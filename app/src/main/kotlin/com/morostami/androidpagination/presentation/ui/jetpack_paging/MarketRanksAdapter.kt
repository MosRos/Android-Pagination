/*
 * *
 *  * Created by Moslem Rostami on 6/18/20 8:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/18/20 8:21 PM
 *
 */

package com.morostami.androidpagination.presentation.ui.jetpack_paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.morostami.androidpagination.domain.model.RankedCoin
import com.morostami.androidpagination.presentation.ui.RankedCoinViewHolder

class MarketRanksAdapter(private val onRankedCoinClick: (RankedCoin, Int) -> Unit) : PagingDataAdapter<RankedCoin, RankedCoinViewHolder>(COIN_COMPARATOR) {

    companion object {
        private val COIN_COMPARATOR = object : DiffUtil.ItemCallback<RankedCoin>() {
            override fun areItemsTheSame(oldItem: RankedCoin, newItem: RankedCoin): Boolean {
                return (oldItem.id == newItem.id)
            }

            override fun areContentsTheSame(oldItem: RankedCoin, newItem: RankedCoin): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RankedCoinViewHolder, position: Int) {
        val coin: RankedCoin? = getItem(position)
        coin?.let {
            holder.databinding.rankedCoin = coin
            holder.bind(coin, position, onRankedCoinClick)
            holder.databinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankedCoinViewHolder {
        val binding = com.morostami.androidpagination.databinding.ListItemRankedCoinBinding.inflate(LayoutInflater.from(parent.context))
        return RankedCoinViewHolder(binding)
    }
}