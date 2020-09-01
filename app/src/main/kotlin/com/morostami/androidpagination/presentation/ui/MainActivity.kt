package com.morostami.androidpagination.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morostami.androidpagination.R
import com.morostami.androidpagination.presentation.ui.jetpack_paging.Paging3Fragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, Paging3Fragment.newInstance())
                    .commitNow()
        }
    }
}