package com.alirezaafkar.phuzei.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Alireza Afkar on 14/4/2018AD.
 */
class InfiniteScrollListener(private val listener: () -> Unit) :
    androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

    private var loading = false
    private var previousTotal = 0

    override fun onScrolled(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        dx: Int,
        dy: Int
    ) {
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
        val spanCount = getSpanCount(recyclerView)
        val firstVisibleItems = getFirstVisibleItem(recyclerView, IntArray(spanCount))

        if (totalItemCount == 0 && totalItemCount < previousTotal) {
            previousTotal = 0
        }

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItems + visibleItemCount) {
            loading = true
            listener()
        }
    }

    private fun getFirstVisibleItem(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        into: IntArray
    ): Int {
        return when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findFirstCompletelyVisibleItemPositions(
                into
            )[0]
            else -> 0
        }
    }

    private fun getSpanCount(recyclerView: androidx.recyclerview.widget.RecyclerView): Int {
        return when (val layoutManager = recyclerView.layoutManager) {
            is GridLayoutManager -> layoutManager.spanCount
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> 1
        }
    }
}
