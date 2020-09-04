/*
 * *
 *  * Created by Moslem Rostami on 4/9/20 8:48 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 4/9/20 8:48 PM
 *
 */

package com.morostami.androidpagination.data.local

import androidx.paging.PagingSource
import com.morostami.androidpagination.data.local.dao.RemoteKeysDao
import com.morostami.androidpagination.domain.model.CoinsRemoteKeys
import com.morostami.androidpagination.domain.model.RankedCoin
import javax.inject.Inject

class MarketLocalDataSource @Inject constructor(
    private val cryptoDataBase: CryptoDataBase) : RemoteKeysDao {

    private val cryptoMarketDao by lazy { cryptoDataBase.cryptoMarketDao() }
    private val remoteKeysDao by lazy { cryptoDataBase.remoteKeysDao() }

    // RankedCoins
    suspend fun insertRankedCoins(coinsList: List<RankedCoin>) = cryptoMarketDao.insertRankedCoins(coinsList)

    suspend fun insertRankedCoin(rankedCoin: RankedCoin) = cryptoMarketDao.insertRankedCoin(rankedCoin)

    suspend fun getAllRankedCoins(): List<RankedCoin> = cryptoMarketDao.getAllRankedCoins()

    suspend fun getRankedCoinsList(offset: Int, limit: Int): List<RankedCoin> = cryptoMarketDao.getRankedCoinsList(offset, limit)

    fun getPagedRankedCoins(): PagingSource<Int, RankedCoin> = cryptoMarketDao.getPagedRankedCoins()

    suspend fun deleteRankedCoin(rankedCoin: RankedCoin) = cryptoMarketDao.deleteRankedCoin(rankedCoin)

    suspend fun deleteRankedCoins(coinsList: List<RankedCoin>) = cryptoMarketDao.deleteRankedCoins(coinsList)

    suspend fun deleteAllRankedCoins() = cryptoMarketDao.deleteAllRankedCoins()

    suspend fun getRemoteKeysCoinId(coinId: String): CoinsRemoteKeys? = remoteKeysDao.remoteKeysCoinId(coinId)

    override suspend fun insertAllRemoteKeys(remoteKeys: List<CoinsRemoteKeys>) = remoteKeysDao.insertAllRemoteKeys(remoteKeys)

    override suspend fun insertRemoteKey(remoteKey: CoinsRemoteKeys) = remoteKeysDao.insertRemoteKey(remoteKey)

    override suspend fun remoteKeysCoinId(coinId: String): CoinsRemoteKeys? = remoteKeysDao.remoteKeysCoinId(coinId)

    override suspend fun clearCoinsRemoteKeys() = remoteKeysDao.clearCoinsRemoteKeys()
}