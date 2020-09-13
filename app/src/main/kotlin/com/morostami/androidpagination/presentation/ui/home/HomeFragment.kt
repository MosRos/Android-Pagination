package com.morostami.androidpagination.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.morostami.androidpagination.R
import com.morostami.androidpagination.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var homeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding?.apply {
            btnGooglePaging.setOnClickListener {
                Navigation.findNavController(homeBinding.root).navigate(R.id.action_fragment_home_to_fragment_paging)
            }

            btnManualPagination.setOnClickListener {
                Navigation.findNavController(homeBinding.root).navigate(R.id.action_fragment_home_to_fragment_manual)
            }

            btnRxPagination.setOnClickListener {
                Navigation.findNavController(homeBinding.root).navigate(R.id.action_fragment_home_to_fragment_rx_pagination)
            }
        }
    }
}