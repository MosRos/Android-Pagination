package com.morostami.androidpagination.presentation.ui.manual_pagination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.morostami.androidpagination.domain.ManualPaginationUseCase
import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@ActivityScoped
class ManualPaginationViewModel @Inject constructor(private val manualPaginationUseCase: ManualPaginationUseCase) : ViewModel() {

    var marketRanks: LiveData<List<RankedCoin>> = MutableLiveData()

    fun getRanks(page: Int) {
        marketRanks = liveData {
            manualPaginationUseCase.getRanks(page).collect{ result ->
                when(result) {
                    is Result.Success -> {
                        emit(result.data)
                    }

                    is Result.Error -> {  }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }
}