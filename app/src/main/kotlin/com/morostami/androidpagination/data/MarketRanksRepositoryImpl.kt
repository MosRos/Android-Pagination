package com.morostami.androidpagination.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haroldadmin.cnradapter.NetworkResponse
import com.morostami.androidpagination.data.local.MarketLocalDataSource
import com.morostami.androidpagination.data.remote.RemoteDataSource
import com.morostami.androidpagination.data.remote.responses.CoinGeckoApiError
import com.morostami.androidpagination.domain.MarketRanksRepository
import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.CoinsRemoteKeys
import com.morostami.androidpagination.domain.model.RankedCoin
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
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
            if (!localData.isNullOrEmpty()){
                emit(Result.Success(localData))
            }
            val networkRespons: NetworkResponse<List<RankedCoin>, CoinGeckoApiError> =
                remoteDataSource.getPagedMarketRanks(vs_currency = "usd", page = offset, per_page = PAGE_SIZE)

            when(networkRespons) {
                is NetworkResponse.Success -> {
                    GlobalScope.async(Dispatchers.IO){
                        networkRespons.body.forEach { rank ->
                            marketLocalDataSource.insertRankedCoin(rank)
                        }
                    }
                    emit(Result.Success(networkRespons.body))
                }
                else -> {emit(Result.Error(Exception("Bla")))}
            }
        }
//        return object : NetworkBoundResource<List<RankedCoin>, List<RankedCoin>, CoinGeckoApiError>(){
//            init {
//                limit = DEFAULD_PAGE_SIZE
//            }
//            override suspend fun getFromDatabase(
//                isRefreshed: Boolean,
//                limit: Int,
//                offset: Int
//            ): List<RankedCoin>? {
//                return marketLocalDataSource.getRankedCoinsList(offset, DEFAULD_PAGE_SIZE)
//            }
//
//            override suspend fun validateCache(cachedData: List<RankedCoin>?): Boolean {
//                return !cachedData.isNullOrEmpty()
//            }
//
//            override suspend fun getFromApi(): NetworkResponse<List<RankedCoin>, CoinGeckoApiError> {
//                return remoteDataSource.getPagedMarketRanks("usd", offset, DEFAULD_PAGE_SIZE)
//            }
//
//            override suspend fun persistData(apiData: List<RankedCoin>) {
//                withContext(Dispatchers.IO){
//                    apiData.forEach { coin ->
//                        coin?.apply {
//                            pageKey = offset
//                        }
//                        marketLocalDataSource.insertRankedCoin(coin)
//                    }
//                }
//            }
//
//
//        }.flow()
    }

    override fun getRanksRx(offset: Int): Flowable<List<RankedCoin>> {
        val responseObservable = Flowable.concatArray(
            loadFromDb(offset),
            fetchFromRemote(offset)
        )

        return responseObservable
    }

    private fun loadFromDb(offset: Int) : Flowable<List<RankedCoin>> {
        return marketLocalDataSource.getRankedCoinsListRx(offset = offset, limit = DEFAULD_PAGE_SIZE).toFlowable()
    }

    private fun fetchFromRemote(offset: Int) : Flowable<List<RankedCoin>> {
        val response = remoteDataSource.getPagedMarketRanksRx(
            vs_currency = "usd",
            page = offset,
            per_page = DEFAULD_PAGE_SIZE
        )
            .doOnSuccess{coins -> saveResults(coins, offset)}

        return response.toFlowable()

    }

    private fun saveResults(coins: List<RankedCoin>, offset: Int) {
        Completable.fromCallable {
            coins.forEach { coin ->
                val rankedCoin = coin.apply {
                    pageKey = offset
                }

                marketLocalDataSource.insertRankedCoinRx(rankedCoin)
            }

        }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
    }
}