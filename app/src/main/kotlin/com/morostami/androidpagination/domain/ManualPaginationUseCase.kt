package com.morostami.androidpagination.domain

import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.RankedCoin
import io.reactivex.Flowable
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManualPaginationUseCase @Inject constructor(private val marketRanksRepository: MarketRanksRepository) {

    fun getRanks(offset: Int) : Flow<Result<List<RankedCoin>>> = marketRanksRepository.getRanks(offset)

    fun getRanksRx(offset: Int) : Observable<List<RankedCoin>> = marketRanksRepository.getRanksRx(offset)
}