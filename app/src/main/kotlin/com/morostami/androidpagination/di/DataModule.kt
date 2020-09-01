package com.morostami.androidpagination.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.morostami.androidpagination.data.MarketRanksMediator
import com.morostami.androidpagination.data.MarketRanksRepositoryImpl
import com.morostami.androidpagination.data.local.CryptoDataBase
import com.morostami.androidpagination.data.local.MarketLocalDataSource
import com.morostami.androidpagination.data.remote.RemoteDataSource
import com.morostami.androidpagination.domain.MarketRanksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideCoinsRoomDb(application: Application) : CryptoDataBase {
        return CryptoDataBase.getInstance(application)
//        return Room.databaseBuilder(application.applicationContext, CryptoDataBase::class.java, "coins_db")
//            .build()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun provideMarketRanksRepository(
        remoteDataSource: RemoteDataSource,
        marketLocalDataSource: MarketLocalDataSource,
        marketRanksMediator: MarketRanksMediator
    ) : MarketRanksRepository {
        return MarketRanksRepositoryImpl(remoteDataSource, marketLocalDataSource, marketRanksMediator)
    }
}