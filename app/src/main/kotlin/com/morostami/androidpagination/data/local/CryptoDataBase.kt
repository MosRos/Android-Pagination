package com.morostami.androidpagination.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.morostami.androidpagination.data.local.dao.CryptoMarketDao
import com.morostami.androidpagination.data.local.dao.RemoteKeysDao
import com.morostami.androidpagination.domain.model.CoinsRemoteKeys
import com.morostami.androidpagination.domain.model.RankedCoin

@Database(entities = [RankedCoin::class, CoinsRemoteKeys::class], version = 1, exportSchema = false)
abstract class CryptoDataBase : RoomDatabase() {

    abstract fun cryptoMarketDao() : CryptoMarketDao
    abstract fun remoteKeysDao() : RemoteKeysDao
    companion object {
        @Volatile private var INSTANCE: CryptoDataBase? = null
        private const val db_name = "coins_db"

        fun getInstance(context: Context) : CryptoDataBase {
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: buildDatabase(context).also{ INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, CryptoDataBase::class.java, db_name)
                .build()
    }
}