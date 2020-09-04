package com.morostami.androidpagination.presentation.ui.manual_pagination

import androidx.lifecycle.ViewModel
import com.morostami.androidpagination.domain.ManualPaginationUseCase
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class ManualPaginationViewModel @Inject constructor(private val manualPaginationUseCase: ManualPaginationUseCase) : ViewModel() {

}