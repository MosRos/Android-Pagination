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
import io.reactivex.Observable
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

    override fun getRanksRx(offset: Int): Observable<List<RankedCoin>> {
        var apiResponse: Observable<List<RankedCoin>>? = null

        if (NetworkUtils.isConnected()) {
            apiResponse = fetchFromRemote(offset)

        }
        var dbResponse: Observable<List<RankedCoin>> = loadFromDb(offset)

        return if (NetworkUtils.isConnected()) Observable.concatArrayEager(
            dbResponse,
            apiResponse
        ) else dbResponse
    }

    private fun loadFromDb(offset: Int): Observable<List<RankedCoin>> {
        return marketLocalDataSource
            .getRankedCoinsListRx(
            offset = offset,
            limit = DEFAULD_PAGE_SIZE)
            .toObservable()
            .onErrorReturn() {
                listOf()
            }
            .doOnNext {coins ->
                //Print log it.size :)
                Timber.e("REPOSITORY DB *** ${coins.size}")
            }

    }

    private fun fetchFromRemote(offset: Int): Observable<List<RankedCoin>> {
        var response: Observable<List<RankedCoin>>? = null
        try {
            response = remoteDataSource
                .getPagedMarketRanksRx(
                vs_currency = "usd",
                page = offset,
                per_page = DEFAULD_PAGE_SIZE)
                .toObservable()
                .onErrorReturn() { t ->
                null
            }.doOnNext { coins ->
                    Timber.e("REPOSITORY API *** ${coins.size}")
                    saveResults(coins, offset)
            }
        } catch (e: OnErrorNotImplementedException) {
            Timber.e("Repository ${e.message}")
        } catch (e: Exception) {
            Timber.e("Repository ${e.message}")
        }

        return response ?: loadFromDb(offset)
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