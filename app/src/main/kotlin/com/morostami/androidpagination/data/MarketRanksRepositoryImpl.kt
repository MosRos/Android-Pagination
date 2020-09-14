package com.morostami.androidpagination.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haroldadmin.cnradapter.NetworkResponse
import com.morostami.androidpagination.data.local.MarketLocalDataSource
import com.morostami.androidpagination.data.remote.RemoteDataSource
import com.morostami.androidpagination.data.remote.responses.CoinGeckoApiError
import com.morostami.androidpagination.data.utils.NetworkUtils
import com.morostami.androidpagination.domain.MarketRanksRepository
import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.RankedCoin
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class MarketRanksRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val marketLocalDataSource: MarketLocalDataSource,
    private val marketRanksMediator: MarketRanksMediator
) : MarketRanksRepository {

    companion object {
        const val DEFAULD_PAGE_SIZE = 50
        const val INITIAL_NETWORK_OFFSET = 1
    }

    private var compositeDisposable = CompositeDisposable()

    override fun getPagedRanks(): Flow<PagingData<RankedCoin>> {

        val pagingFactory = { marketLocalDataSource.getPagedRankedCoins() }

        return Pager(
            config = PagingConfig(pageSize = DEFAULD_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingFactory,
            remoteMediator = marketRanksMediator
        ).flow
    }

    override fun getRanks(offset: Int): Flow<Result<List<RankedCoin>>> {

        return flow {
            emit(Result.Loading)
            val localData = marketLocalDataSource.getRankedCoinsList(offset, PAGE_SIZE)
            if (!localData.isNullOrEmpty()) {
                emit(Result.Success(localData))
            }
            val networkRespons: NetworkResponse<List<RankedCoin>, CoinGeckoApiError> =
                remoteDataSource.getPagedMarketRanks(
                    vs_currency = "usd",
                    page = offset,
                    per_page = PAGE_SIZE
                )

            when (networkRespons) {
                is NetworkResponse.Success -> {
                    val coins: List<RankedCoin> = networkRespons.body
                    saveResults(coins, offset)
                    emit(Result.Success(coins))
                }
                else -> {
                    emit(Result.Error(Exception("Bla")))
                }
            }
        }
    }

    override fun getRanksRx(offset: Int): Flowable<List<RankedCoin>> {

        return if (NetworkUtils.isOnline()) {
            Flowable.concatArrayEager(
                loadFromDb(offset),
                fetchFromRemote(offset)
            )
        } else {
            loadFromDb(offset)
        }
    }

    private fun loadFromDb(offset: Int): Flowable<List<RankedCoin>> {
        return marketLocalDataSource.getRankedCoinsListRx(
            offset = offset,
            limit = DEFAULD_PAGE_SIZE
        )
            .onErrorReturn() {
                listOf()
            }
            .toFlowable()
    }

    private fun loadFromDbSync(offset: Int) : List<RankedCoin> {
        return marketLocalDataSource.getRankedCoinsListSync(
            offset = offset,
            limit = DEFAULD_PAGE_SIZE
        )
    }

    private fun fetchFromRemote(offset: Int): Flowable<List<RankedCoin>> {
        var response: Single<List<RankedCoin>>? = null
        try {
            response = remoteDataSource.getPagedMarketRanksRx(
                vs_currency = "usd",
                page = offset,
                per_page = DEFAULD_PAGE_SIZE
            ).onErrorReturn() { t ->
                null
            }.doOnSuccess { coins ->
                saveResults(coins, offset)
            }
        } catch (e: OnErrorNotImplementedException) {
            Timber.e("Repository ${e.message}")
        } catch (e: Exception) {
            Timber.e("Repository ${e.message}")
        }

        if (response == null)  {
            response = marketLocalDataSource.getRankedCoinsListRx(
                offset = offset,
                limit = DEFAULD_PAGE_SIZE
            )
        }
        response?.doOnSuccess { coins ->
            saveResults(coins, offset)
        }?.subscribeOn(Schedulers.io())?.subscribe()

        return response?.toFlowable() ?: loadFromDb(offset)
    }

    private fun saveResults(coins: List<RankedCoin>, offset: Int) {
        GlobalScope.async(Dispatchers.IO) {
            val dbCoins: List<RankedCoin> = coins.map {
                rankedCoin: RankedCoin -> rankedCoin.apply {
                pageKey = offset
            }
            }
            marketLocalDataSource.insertRankedCoins(dbCoins)
        }
    }
}