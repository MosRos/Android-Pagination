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
import io.reactivex.Completable
import io.reactivex.Single

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

    // RxJava
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRankedCoinsRx(coinsList: List<RankedCoin>) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRankedCoinRx(rankedCoin: RankedCoin) : Completable

    @Query("SELECT * FROM COINS ORDER BY marketCapRank ASC LIMIT :limit OFFSET :offset")
    fun getRankedCoinsListRx(offset: Int, limit: Int): Single<List<RankedCoin>>

}