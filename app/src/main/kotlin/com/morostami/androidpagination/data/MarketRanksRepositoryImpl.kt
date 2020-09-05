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
import com.morostami.androidpagination.domain.model.RankedCoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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

    override fun getPagedRanks(): Flow<PagingData<RankedCoin>> {

        val pagingFactory = { marketLocalDataSource.getPagedRankedCoins() }

        return Pager(
            config = PagingConfig(pageSize = DEFAULD_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingFactory,
            remoteMediator = marketRanksMediator
        ).flow
    }

    override fun getRanks(offset: Int): Flow<Result<List<RankedCoin>>> {

        return object : NetworkBoundResource<List<RankedCoin>, List<RankedCoin>, CoinGeckoApiError>(){
            init {
                limit = DEFAULD_PAGE_SIZE
            }
            override suspend fun getFromDatabase(
                isRefreshed: Boolean,
                limit: Int,
                offset: Int
            ): List<RankedCoin>? {
                return marketLocalDataSource.getRankedCoinsList(offset, DEFAULD_PAGE_SIZE)
            }

            override suspend fun validateCache(cachedData: List<RankedCoin>?): Boolean {
                return !cachedData.isNullOrEmpty()
            }

            override suspend fun getFromApi(): NetworkResponse<List<RankedCoin>, CoinGeckoApiError> {
                return remoteDataSource.getPagedMarketRanks("usd", offset, DEFAULD_PAGE_SIZE)
            }

            override suspend fun persistData(apiData: List<RankedCoin>) {
                withContext(Dispatchers.IO){
                    apiData.forEach { coin ->
                        coin?.apply {
                            pageKey = offset
                        }
                        marketLocalDataSource.insertRankedCoin(coin)
                    }
                }
            }


        }.flow()
    }
}