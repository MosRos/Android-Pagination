package com.morostami.androidpagination.presentation.ui.manual_pagination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.morostami.androidpagination.R

class ManualPaginationFragment : Fragment() {

    companion object {
        fun newInstance() = ManualPaginationFragment()
    }

    private lateinit var viewModel: ManualPaginationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManualPaginationViewModel::class.java)
    }

}