/*
 * *
 *  * Created by Moslem Rostami on 7/15/20 9:39 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 7/15/20 4:30 PM
 *
 */

package com.morostami.androidpagination.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.morostami.androidpagination.domain.model.RankedCoin

@Dao
interface CryptoMarketDao {

    // For RankedCoins
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankedCoins(coinsList: List<RankedCoin>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRankedCoin(rankedCoin: RankedCoin)

    @Query("SELECT * FROM COINS ORDER BY marketCapRank ASC")
    suspend fun getAllRankedCoins(): List<RankedCoin>

    @Query("SELECT * FROM COINS ORDER BY marketCapRank ASC LIMIT :limit OFFSET :offset")
    suspend fun getRankedCoinsList(offset: Int, limit: Int): List<RankedCoin>

    @Query("SELECT * FROM COINS ORDER BY marketCapRank ASC")
    fun getPagedRankedCoins(): PagingSource<Int, RankedCoin>

    @Delete
    suspend fun deleteRankedCoin(rankedCoin: RankedCoin)

    @Delete
    suspend fun deleteRankedCoins(coinsList: List<RankedCoin>)

    @Query("DELETE FROM COINS")
    suspend fun deleteAllRankedCoins()
}