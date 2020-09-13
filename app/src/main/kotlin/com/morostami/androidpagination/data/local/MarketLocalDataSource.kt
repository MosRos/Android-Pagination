/*
 * *
 *  * Created by Moslem Rostami on 4/9/20 8:48 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 4/9/20 8:48 PM
 *
 */

package com.morostami.androidpagination.data.local

import androidx.paging.PagingSource
import com.morostami.androidpagination.data.local.dao.CryptoMarketDao
import com.morostami.androidpagination.data.local.dao.RemoteKeysDao
import com.morostami.androidpagination.domain.model.CoinsRemoteKeys
import com.morostami.androidpagination.domain.model.RankedCoin
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MarketLocalDataSource @Inject constructor(
    private val cryptoDataBase: CryptoDataBase) : RemoteKeysDao, CryptoMarketDao {

    private val cryptoMarketDao by lazy { cryptoDataBase.cryptoMarketDao() }
    private val remoteKeysDao by lazy { cryptoDataBase.remoteKeysDao() }

    // RankedCoins
    override suspend fun insertRankedCoins(coinsList: List<RankedCoin>) = cryptoMarketDao.insertRankedCoins(coinsList)

    override suspend fun insertRankedCoin(rankedCoin: RankedCoin) = cryptoMarketDao.insertRankedCoin(rankedCoin)

    override suspend fun getAllRankedCoins(): List<RankedCoin> = cryptoMarketDao.getAllRankedCoins()

    override suspend fun getRankedCoinsList(offset: Int, limit: Int): List<RankedCoin> = cryptoMarketDao.getRankedCoinsList(offset, limit)

    override fun getPagedRankedCoins(): PagingSource<Int, RankedCoin> = cryptoMarketDao.getPagedRankedCoins()

    override suspend fun deleteRankedCoin(rankedCoin: RankedCoin) = cryptoMarketDao.deleteRankedCoin(rankedCoin)

    override suspend fun deleteRankedCoins(coinsList: List<RankedCoin>) = cryptoMarketDao.deleteRankedCoins(coinsList)

    override suspend fun deleteAllRankedCoins() = cryptoMarketDao.deleteAllRankedCoins()

    suspend fun getRemoteKeysCoinId(coinId: String): CoinsRemoteKeys? = remoteKeysDao.remoteKeysCoinId(coinId)

    override suspend fun insertAllRemoteKeys(remoteKeys: List<CoinsRemoteKeys>) = remoteKeysDao.insertAllRemoteKeys(remoteKeys)

    override suspend fun insertRemoteKey(remoteKey: CoinsRemoteKeys) = remoteKeysDao.insertRemoteKey(remoteKey)

    override suspend fun remoteKeysCoinId(coinId: String): CoinsRemoteKeys? = remoteKeysDao.remoteKeysCoinId(coinId)

    override suspend fun clearCoinsRemoteKeys() = remoteKeysDao.clearCoinsRemoteKeys()

    override fun insertRankedCoinsRx(coinsList: List<RankedCoin>): Completable = cryptoMarketDao.insertRankedCoinsRx(coinsList)

    override fun insertRankedCoinRx(rankedCoin: RankedCoin): Completable = cryptoMarketDao.insertRankedCoinRx(rankedCoin)

    override fun getRankedCoinsListRx(offset: Int, limit: Int): Single<List<RankedCoin>> = cryptoMarketDao.getRankedCoinsListRx(offset, limit)
}