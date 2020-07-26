package com.morostami.androidpagination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.morostami.androidpagination.ui.paging_3.Paging3Fragment

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