package com.morostami.androidpagination.domain

import androidx.paging.PagingData
import com.morostami.androidpagination.domain.model.RankedCoin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingRanksUseCase @Inject constructor(private val marketRanksRepository: MarketRanksRepository) {

    fun getPagedRanks() : Flow<PagingData<RankedCoin>> = marketRanksRepository.getPagedRanks()
}