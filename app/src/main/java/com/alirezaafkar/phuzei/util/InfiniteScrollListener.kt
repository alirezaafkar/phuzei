package com.alirezaafkar.phuzei.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


/**
 * Created by Alireza Afkar on 14/4/2018AD.
 */
class InfiniteScrollListener(private val listener: () -> Unit) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    private var loading = false
    private var previousTotal = 0

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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

    private fun getFirstVisibleItem(recyclerView: androidx.recyclerview.widget.RecyclerView, into: IntArray): Int {
        val layoutManager = recyclerView.layoutManager
        return when (layoutManager) {
            is androidx.recyclerview.widget.LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is androidx.recyclerview.widget.GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
                layoutManager.findFirstCompletelyVisibleItemPositions(into)[0]
            }
            else -> 0
        }
    }

    private fun getSpanCount(recyclerView: androidx.recyclerview.widget.RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        return when (layoutManager) {
            is androidx.recyclerview.widget.GridLayoutManager -> layoutManager.spanCount
            is androidx.recyclerview.widget.StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> 1
        }
    }
}
