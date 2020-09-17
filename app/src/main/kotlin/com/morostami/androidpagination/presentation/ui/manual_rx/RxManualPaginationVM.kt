package com.morostami.androidpagination.presentation.ui.manual_rx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.morostami.androidpagination.domain.ManualPaginationUseCase
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@ActivityScoped
class RxManualPaginationVM @Inject constructor(private val manualPaginationUseCase: ManualPaginationUseCase) : ViewModel() {

    var marketRanks: MutableLiveData<List<RankedCoin>> = MutableLiveData()
    var ranksDisposable: DisposableObserver<List<RankedCoin>>? = null
    fun getRanks(page: Int) {
        Timber.e("Request for New Page : $page")
        ranksDisposable = object :DisposableObserver<List<RankedCoin>>() {
            override fun onComplete() {
                Timber.e("RxVm onComplete dispose")
            }

            override fun onNext(coins: List<RankedCoin>) {
                Timber.e("RxVm recieved coins ${coins.size}")
                marketRanks.postValue(coins)
            }

            override fun onError(e: Throwable) {
                Timber.e("RxVm ${e.message}")
            }
        }

        return manualPaginationUseCase.getRanksRx(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(ranksDisposable!!)
//        disposable.add(
//            manualPaginationUseCase.getRanksRx(page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .onErrorReturn { t->
//                    listOf()
//                }
//                .doOnError { t ->
//                    Timber.e("RxVm ${t.message}")
//                }
//                .subscribe { coins -> marketRanks.postValue(coins) }
//        )
    }

    override fun onCleared() {
        super.onCleared()
        if (ranksDisposable != null) {
            if (ranksDisposable!!.isDisposed) {
                ranksDisposable?.dispose()
            }
        }
    }
}