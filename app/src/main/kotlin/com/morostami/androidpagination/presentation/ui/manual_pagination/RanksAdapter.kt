package com.morostami.androidpagination.presentation.ui.manual_pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.morostami.androidpagination.domain.model.RankedCoin
import com.morostami.androidpagination.presentation.ui.RankedCoinViewHolder

class RanksAdapter(private var onRankedCoinClicked: (RankedCoin, Int) -> Unit) : ListAdapter<RankedCoin, RankedCoinViewHolder>(
    COIN_COMPARATOR) {

    companion object {
        private val COIN_COMPARATOR = object : DiffUtil.ItemCallback<RankedCoin>() {
            override fun areItemsTheSame(oldItem: RankedCoin, newItem: RankedCoin): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: RankedCoin, newItem: RankedCoin): Boolean = false
//                oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: RankedCoinViewHolder, position: Int) {
        val coin: RankedCoin? = getItem(position)
        coin?.let {
            holder.bind(coin, position, onRankedCoinClicked)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankedCoinViewHolder {
        val binding = com.morostami.androidpagination.databinding.ListItemRankedCoinBinding.inflate(
            LayoutInflater.from(parent.context))
        return RankedCoinViewHolder(binding)
    }
}