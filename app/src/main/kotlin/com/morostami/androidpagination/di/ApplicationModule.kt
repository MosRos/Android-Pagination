package com.morostami.androidpagination.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.morostami.androidpagination.data.preferences.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application) : SharedPreferences {
        return app.getSharedPreferences("arch_sample", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePreferenceHelper(sharedPreferences: SharedPreferences) : PreferencesHelper {
        return PreferencesHelper(
            sharedPreferences
        )
    }
}