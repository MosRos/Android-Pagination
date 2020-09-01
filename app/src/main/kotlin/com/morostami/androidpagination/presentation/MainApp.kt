package com.morostami.androidpagination.presentation

import android.app.Application
import android.util.Log
import com.morostami.androidpagination.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import org.jetbrains.annotations.NonNls
import timber.log.Timber

@HiltAndroidApp
class MainApp : Application() {

    companion object {
        lateinit var instance: MainApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    fun getAppContext() : Application {
        return instance
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?, @NonNls message: String,
            t: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
        }
    }
}