package com.morostami.androidpagination.presentation.ui.manual_pagination

import androidx.recyclerview.widget.*

fun RecyclerView.disableAnimation() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}


fun RecyclerView.LayoutManager.findFirstVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findFirstVisibleItemPosition()
        is GridLayoutManager -> findFirstVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val firstVisibleItemPositions = findFirstVisibleItemPositions(null)

            // get minimum element within the list
            var minSize = 0
            for (i in firstVisibleItemPositions.indices) {
                if (i == 0) {
                    minSize = firstVisibleItemPositions[i]
                } else if (firstVisibleItemPositions[i] < minSize) {
                    minSize = firstVisibleItemPositions[i]
                }
            }
            return minSize
        }
        else -> throw IllegalStateException("Recycler view layout manager is not supported")
    }
}

fun RecyclerView.LayoutManager.findLastVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is GridLayoutManager -> findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val lastVisibleItemPositions = findLastVisibleItemPositions(null)

            // get maximum element within the list
            var maxSize = 0
            for (i in lastVisibleItemPositions.indices) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i]
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i]
                }
            }
            return maxSize
        }
        else -> throw IllegalStateException("Recycler view layout manager is not supported")
    }
}