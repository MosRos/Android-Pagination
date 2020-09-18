package com.morostami.androidpagination.presentation.ui.manual_rx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.morostami.androidpagination.domain.ManualPaginationUseCase
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class RxManualPaginationVM @Inject constructor(private val manualPaginationUseCase: ManualPaginationUseCase) : ViewModel() {

    var marketRanks: MutableLiveData<List<RankedCoin>> = MutableLiveData()
    var ranksDisposable: CompositeDisposable = CompositeDisposable()
    fun getRanks(page: Int) {
        Timber.e("Request for New Page : $page")
        ranksDisposable.add(
            manualPaginationUseCase.getRanksRx(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { t->
                    listOf()
                }
                .doOnComplete {
                    Timber.e("RxVm onComplete dispose")
                }
                .doOnError { t ->
                    Timber.e("RxVm ${t.message}")
                }
                .subscribe { coins ->
                    Timber.e("RxVm recieved coins ${coins.size}")
                    marketRanks.postValue(coins)
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        ranksDisposable.dispose()
    }
}