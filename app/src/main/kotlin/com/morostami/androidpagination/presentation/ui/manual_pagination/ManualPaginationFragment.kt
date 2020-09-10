package com.morostami.androidpagination.presentation.ui.manual_pagination

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.morostami.androidpagination.R
import com.morostami.androidpagination.databinding.FragmentMarketRankBinding
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ManualPaginationFragment : Fragment(), LoadMoreObserver {

    companion object {
        fun newInstance() = ManualPaginationFragment()
    }

    @Inject
    lateinit var viewModel: ManualPaginationViewModel
    private lateinit var mContext: Context
    private lateinit var dataBinding: FragmentMarketRankBinding
    private lateinit var rankRecycler: RecyclerView
    private lateinit var ranksAdapter: RanksAdapter
    private lateinit var endlessScrollListener: EndlessScrollListener
    private var recyclerviewState: Parcelable? = null
    private var coins: ArrayList<RankedCoin> = ArrayList()

    private val onCoinClicked: (RankedCoin, Int) -> Unit = { coin, position ->
        Toast.makeText(context, "${coin.name} clicked at $position", Toast.LENGTH_SHORT).show()
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
        viewModel.getRanks(1)
        setObservables()
    }

    private fun initWidgets() {
        ranksAdapter = RanksAdapter(onCoinClicked)
        ranksAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        val llManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        endlessScrollListener = EndlessScrollListener(
            layoutManager = llManager,
            visibleThreshold = 5,
            includeEmptyState = false,
            loadMoreObserver = this)

        rankRecycler = dataBinding.rankRecycler
        with(rankRecycler) {
            layoutManager = llManager
            addOnScrollListener(endlessScrollListener)
            adapter = ranksAdapter
            this.disableAnimation()
        }
    }

    private fun setObservables() {
        viewModel.marketRanks.observe(viewLifecycleOwner, Observer { ranks ->
            ranks?.let {
                coins.addAll(ranks)
                coins.sortBy { rankedCoin ->
                    rankedCoin.marketCapRank
                }
                updateRecycler(coins)
            }
        })
    }

    private fun updateRecycler(items: List<RankedCoin>) {
        if (items.isEmpty()) {
            viewModel.getRanks(1)
            return
        }
        var sortedCoins: List<RankedCoin> = items.distinctBy { rankedCoin ->
            rankedCoin.id
        }
        recyclerviewState = rankRecycler.layoutManager?.onSaveInstanceState()
        ranksAdapter.submitList(sortedCoins)
        rankRecycler.layoutManager?.let {llmanager ->
            llmanager.onRestoreInstanceState(recyclerviewState)
        }

        // for smooth performance
//        lifecycleScope.launch {
////            venueListAdapter.setState(LoadingState.LOADED)
//            delay(50)
//            ranksAdapter.submitList(items)
//            rankRecycler.layoutManager?.let {llmanager ->
//                llmanager.onRestoreInstanceState(recyclerviewState)
//            }
//        }
    }

    override fun onLoadMore(currentPage: Int) {
        Toast.makeText(mContext, "request next page ${currentPage+1}", Toast.LENGTH_SHORT).show()
        viewModel.getRanks(currentPage+1)
    }

    override fun onLoadingStateChanged(loading: Boolean) {
        Toast.makeText(mContext, "Loading State changed $loading", Toast.LENGTH_SHORT).show()
//        if (loading){
//            venueListAdapter.setState(LoadingState.LOADING)
//        } else {
//            venueListAdapter.setState(LoadingState.LOADED)
//        }
    }
}