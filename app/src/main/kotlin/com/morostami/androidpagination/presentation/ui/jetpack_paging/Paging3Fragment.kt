package com.morostami.androidpagination.presentation.ui.jetpack_paging

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morostami.androidpagination.R
import com.morostami.androidpagination.databinding.FragmentMarketRankBinding
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class Paging3Fragment : Fragment() {

    companion object {
        fun newInstance() = Paging3Fragment()
    }

    @Inject
    lateinit var viewModel: Paging3ViewModel
    private lateinit var mContext: Context
    private lateinit var dataBinding: FragmentMarketRankBinding
    private lateinit var rankRecycler: RecyclerView
    private lateinit var ranksPagingAdapter: RanksPagingAdapter

    private val onItemClicked : (RankedCoin, Int) -> Unit = { coin, position ->
        Toast.makeText(context, "${coin.name} at position $position Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_rank, container, false)
        mContext = context ?: dataBinding.rootLayout.context
        return dataBinding.rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWidgets()
        setObservers()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagedRankedCoins().collect{pagedCoins ->
                pagedCoins?.let {
                    Timber.e("Collected Size is: ${it.toString()}")
                    updateRanksAdapter(pagedCoins)
                }
            }
        }
    }

    private fun initWidgets() {
        val llManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        rankRecycler = dataBinding.rankRecycler
        rankRecycler.layoutManager = llManager

        ranksPagingAdapter = RanksPagingAdapter(onItemClicked)
        rankRecycler.adapter = ranksPagingAdapter
    }

    private fun updateRanksAdapter(coins: PagingData<RankedCoin>) {
        viewLifecycleOwner.lifecycleScope.launch {
            ranksPagingAdapter.submitData(coins)
        }
        dataBinding.progressBar.visibility = View.GONE
    }

}