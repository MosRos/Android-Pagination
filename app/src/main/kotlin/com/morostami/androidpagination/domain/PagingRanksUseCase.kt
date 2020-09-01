package com.morostami.androidpagination.domain

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.morostami.androidpagination.domain.model.RankedCoin
import kotlinx.coroutines.flow.Flow

class PagingRanksUseCase (private val marketRanksRepository: MarketRanksRepository) {

    fun getPagedRanks() : Flow<PagingData<RankedCoin>> = marketRanksRepository.getPagedRanks()
}