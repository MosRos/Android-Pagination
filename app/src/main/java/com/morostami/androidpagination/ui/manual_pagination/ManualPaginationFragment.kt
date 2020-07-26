package com.morostami.androidpagination.ui.manual_pagination

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.morostami.androidpagination.R
import com.morostami.androidpagination.ui.paging_3.Paging3Fragment
import com.morostami.androidpagination.ui.paging_3.Paging3ViewModel

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