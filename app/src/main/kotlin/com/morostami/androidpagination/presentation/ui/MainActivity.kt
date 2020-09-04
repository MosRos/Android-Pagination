package com.morostami.androidpagination.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.morostami.androidpagination.R
import com.morostami.androidpagination.databinding.MainActivityBinding
import com.morostami.androidpagination.presentation.ui.jetpack_paging.Paging3Fragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBinding: MainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        val navController: NavController = Navigation.findNavController(this, R.id.nav_host_container)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            destination?.label?.let {lable ->
                dataBinding.titleTxt.text = lable
            }
        }
    }
}