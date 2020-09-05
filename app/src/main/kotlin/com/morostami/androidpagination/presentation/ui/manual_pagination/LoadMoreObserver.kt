package com.morostami.androidpagination.presentation.ui.manual_pagination

interface LoadMoreObserver {
    fun onLoadingStateChanged(loading: Boolean)
    fun onLoadMore(currentPage: Int)
}