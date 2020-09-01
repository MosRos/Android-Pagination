package com.morostami.androidpagination.presentation.ui.jetpack_paging

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.morostami.androidpagination.R

class Paging3Fragment : Fragment() {

    companion object {
        fun newInstance() = Paging3Fragment()
    }

    private lateinit var viewModel: Paging3ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(Paging3ViewModel::class.java)
    }

}