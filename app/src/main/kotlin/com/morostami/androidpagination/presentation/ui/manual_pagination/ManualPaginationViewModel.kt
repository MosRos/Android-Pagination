package com.morostami.androidpagination.presentation.ui.manual_pagination

import androidx.lifecycle.*
import com.morostami.androidpagination.domain.ManualPaginationUseCase
import com.morostami.androidpagination.domain.base.Result
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@ActivityScoped
class ManualPaginationViewModel @Inject constructor(private val manualPaginationUseCase: ManualPaginationUseCase) : ViewModel() {

    var marketRanks: MutableLiveData<List<RankedCoin>> = MutableLiveData()

    fun getRanks(page: Int) {
        viewModelScope.launch {
            manualPaginationUseCase.getRanks(page).collect{ result ->
                when(result) {
                    is Result.Success -> {
                        marketRanks.postValue(result.data)
                    }

                    is Result.Error -> {  }

                    is Result.Loading -> {

                    }
                }
            }
        }
    }
}