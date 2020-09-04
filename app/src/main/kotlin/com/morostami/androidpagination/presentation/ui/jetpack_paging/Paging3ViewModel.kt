package com.morostami.androidpagination.presentation.ui.jetpack_paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.morostami.androidpagination.domain.PagingRanksUseCase
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class Paging3ViewModel @Inject constructor(private val pagingRanksUseCase: PagingRanksUseCase) : ViewModel() {


    fun getPagedRankedCoins() : Flow<PagingData<RankedCoin>> {
        return pagingRanksUseCase.getPagedRanks().cachedIn(viewModelScope)
    }
}