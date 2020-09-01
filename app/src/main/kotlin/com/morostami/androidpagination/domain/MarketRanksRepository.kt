/*
 * *
 *  * Created by Moslem Rostami on 6/17/20 5:07 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/17/20 5:07 PM
 *
 */

package com.morostami.androidpagination.domain

import androidx.paging.PagingData
import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.RankedCoin
import kotlinx.coroutines.flow.Flow

interface MarketRanksRepository {
    fun getPagedRanks() : Flow<PagingData<RankedCoin>>
    fun getRanks(offset: Int) : Flow<Result<List<RankedCoin>>>
}