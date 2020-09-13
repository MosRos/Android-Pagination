/*
 * *
 *  * Created by Moslem Rostami on 3/19/20 12:50 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 3/19/20 12:50 PM
 *
 */

package com.morostami.androidpagination.data.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.morostami.androidpagination.data.remote.responses.CoinGeckoApiError
import com.morostami.androidpagination.domain.model.RankedCoin
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoService {

    @GET("coins/markets")
    suspend fun getPagedMarketRanks(
        @Query("vs_currency") vs_currency: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
        ) : NetworkResponse<List<RankedCoin>, CoinGeckoApiError>


    @GET("coins/markets")
    fun getPagedMarketRanksRx(
        @Query("vs_currency") vs_currency: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ) : Single<List<RankedCoin>>
}