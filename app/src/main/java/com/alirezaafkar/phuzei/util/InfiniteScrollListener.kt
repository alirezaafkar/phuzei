package com.alirezaafkar.phuzei.util

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


/**
 * Created by Alireza Afkar on 14/4/2018AD.
 */
class InfiniteScrollListener(private val listener: () -> Unit) : RecyclerView.OnScrollListener() {
    private var loading = false
    private var previousTotal = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
        val spanCount = getSpanCount(recyclerView)
        val firstVisibleItems = getFirstVisibleItem(recyclerView, IntArray(spanCount))

        if (totalItemCount == 0) previousTotal = 0

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }

        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItems + visibleItemCount) {
            loading = true
            listener()
        }
    }

    private fun getFirstVisibleItem(recyclerView: RecyclerView, into: IntArray): Int {
        val layoutManager = recyclerView.layoutManager
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                layoutManager.findFirstCompletelyVisibleItemPositions(into)[0]
            }
            else -> 0
        }
    }

    private fun getSpanCount(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        return when (layoutManager) {
            is GridLayoutManager -> layoutManager.spanCount
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> 1
        }
    }
}
