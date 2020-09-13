package com.morostami.androidpagination.presentation.ui.manual_rx

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.morostami.androidpagination.domain.ManualPaginationUseCase
import com.morostami.androidpagination.domain.model.RankedCoin
import dagger.hilt.android.scopes.ActivityScoped
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@ActivityScoped
class RxManualPaginationVM @Inject constructor(private val manualPaginationUseCase: ManualPaginationUseCase) : ViewModel() {

    var marketRanks: MutableLiveData<List<RankedCoin>> = MutableLiveData()
    private var disposable: CompositeDisposable = CompositeDisposable()

    fun getRanks(page: Int) {
//         val response = manualPaginationUseCase.getRanksRx(page)
//            .toObservable()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<List<RankedCoin>> {
//                override fun onComplete() {
//
//                }
//
//                override fun onSubscribe(d: Disposable?) {
//
//                }
//
//                override fun onNext(coins: List<RankedCoin>?) {
//                    marketRanks.postValue(coins)
//                }
//
//                override fun onError(e: Throwable?) {
//
//                }
//            })

        disposable.add(
            manualPaginationUseCase.getRanksRx(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn { t->
                    listOf()
                }
                .subscribe { coins -> marketRanks.postValue(coins) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}