package com.morostami.androidpagination.domain

import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.RankedCoin
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManualPaginationUseCase @Inject constructor(private val marketRanksRepository: MarketRanksRepository) {

    fun getRanks(offset: Int) : Flow<Result<List<RankedCoin>>> = marketRanksRepository.getRanks(offset)

    fun getRanksRx(offset: Int) : Flowable<List<RankedCoin>> = marketRanksRepository.getRanksRx(offset)
}