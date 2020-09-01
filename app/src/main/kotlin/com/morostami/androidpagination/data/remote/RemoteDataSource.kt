package com.morostami.androidpagination.data.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.morostami.androidpagination.data.remote.responses.CoinGeckoApiError
import com.morostami.androidpagination.domain.model.RankedCoin
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val retrofit: Retrofit) : CoinGeckoService {

    private val apiService by lazy { retrofit.create(CoinGeckoService::class.java) }


    override suspend fun getPagedMarketRanks(
        vs_currency: String,
        page: Int,
        per_page: Int
    ): NetworkResponse<List<RankedCoin>, CoinGeckoApiError> = apiService.getPagedMarketRanks(vs_currency = vs_currency, page = page, per_page = per_page)

}