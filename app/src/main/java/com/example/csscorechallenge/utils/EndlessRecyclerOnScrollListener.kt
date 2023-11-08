package com.example.csscorechallenge.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(
    private val linearLayoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    companion object {
        private const val INITIAL_PAGE = 1
    }

    // The total number of items in the dataset after the last load
    private var previousTotal = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // The minimum amount of items to have below your current scroll position before loading more.
    private val visibleThreshold = 5

    private var firstVisibleItem = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var currentPage = INITIAL_PAGE

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = linearLayoutManager.itemCount
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading &&
            totalItemCount != 0 &&
            totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold)
        {
            currentPage++
            onLoadMore(currentPage, true)
            loading = true
        }
    }

    abstract fun onLoadMore(currentPage: Int, isNotSwipe: Boolean)
}