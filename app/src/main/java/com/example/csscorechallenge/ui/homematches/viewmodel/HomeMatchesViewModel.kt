package com.example.csscorechallenge.ui.homematches.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.usecase.GetHomeMatchesUseCase
import com.example.csscorechallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class HomeMatchesViewModel constructor(
    private val getHomeMatchesUseCase: GetHomeMatchesUseCase
) : ViewModel() {

    companion object {
        private const val INITIAL_PAGE = 1
    }

    private var currentPage: Int = INITIAL_PAGE

    private val _showLoadingLiveData by lazy { SingleLiveEvent<Boolean>() }
    val showLoadingLiveData = _showLoadingLiveData

    sealed class GetHomeMatchesState {
        data class BindData(val matchList: List<HomeMatchesDomain>, val swipeToRefresh: Boolean) : GetHomeMatchesState()
        data class AppendData(val matchList: List<HomeMatchesDomain>) : GetHomeMatchesState()
        data class Failure(val throwable: Throwable?) : GetHomeMatchesState()
        object NetworkError : GetHomeMatchesState()
        object TimeoutError : GetHomeMatchesState()
    }

    private val _getHomeMatchesLiveData by lazy { SingleLiveEvent<GetHomeMatchesState>() }
    val getHomeMatchesLiveData: LiveData<GetHomeMatchesState> = _getHomeMatchesLiveData

    fun getHomeMatches(
        page: Int,
        appendData: Boolean = true,
        matchList: List<HomeMatchesDomain>? = null,
        swipeToRefresh: Boolean
    ) {
        if (!appendData) {
            _showLoadingLiveData.postValue(true)
        }

        currentPage = if (page == INITIAL_PAGE) {
            INITIAL_PAGE
        } else {
            page
        }

        viewModelScope.launch {
            getHomeMatchesUseCase.getHomeMatches(currentPage)
                .collect { result ->
                    handleGetHomeMatchesResult(result, appendData, matchList, swipeToRefresh)
                }
        }
    }

    private fun handleGetHomeMatchesResult(
        result: Result<List<HomeMatchesDomain>>,
        appendData: Boolean,
        matchList: List<HomeMatchesDomain>? = null,
        swipeToRefresh: Boolean
    ) {
        val homeMatches = result.getOrNull()
        if (result.isSuccess && homeMatches != null) {
            if (appendData) {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.AppendData(homeMatches)
                )
            } else {
                val mergedMatchesList: MutableList<HomeMatchesDomain> = ArrayList()

                if (matchList != null) {
                    mergedMatchesList.addAll(matchList)
                }
                mergedMatchesList.addAll(homeMatches)

                _getHomeMatchesLiveData.postValue(
                    mergedMatchesList.let { matchList -> GetHomeMatchesState.BindData(matchList, swipeToRefresh) }
                )
            }
            _showLoadingLiveData.postValue(false)
        } else {
            _showLoadingLiveData.postValue(false)
            result.exceptionOrNull()?.let { throwable ->
                handleThrowable(throwable)
            }
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.Failure(throwable)
                )
            }
            is UnknownHostException -> {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.NetworkError
                )
            }
            is SocketTimeoutException -> {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.TimeoutError
                )
            }
            else -> {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.Failure(throwable)
                )
            }
        }
    }

    sealed class GetRunningHomeMatchesState {
        data class BindData(
            val matchList: List<HomeMatchesDomain>,
            val page: Int,
            val appendData: Boolean,
            val isSwipeToRefresh: Boolean
        ) : GetRunningHomeMatchesState()
    }

    private val _getRunningHomeMatchesLiveData by lazy { SingleLiveEvent<GetRunningHomeMatchesState>() }
    val getRunningHomeMatchesLiveData: LiveData<GetRunningHomeMatchesState> = _getRunningHomeMatchesLiveData

    fun getRunningHomeMatches(page: Int, appendData: Boolean, isSwipeToRefresh: Boolean) {
        if (!appendData) {
            _showLoadingLiveData.postValue(true)
        }
        viewModelScope.launch {
            getHomeMatchesUseCase.getRunningHomeMatches()
                .collect { result ->
                    handleGetRunningHomeMatchesResult(
                        result,
                        page,
                        appendData,
                        isSwipeToRefresh
                    )
                }
        }
    }

    private fun handleGetRunningHomeMatchesResult(
        result: Result<List<HomeMatchesDomain>>,
        page: Int,
        appendData: Boolean,
        isSwipeToRefresh: Boolean
    ) {
        val homeMatches = result.getOrNull()
        if (result.isSuccess) {
            _getRunningHomeMatchesLiveData.postValue(
                homeMatches?.let {
                    GetRunningHomeMatchesState.BindData(
                        it,
                        page,
                        appendData,
                        isSwipeToRefresh
                    )
                }
            )
        } else {
            result.exceptionOrNull()?.let { throwable ->
                handleThrowable(throwable)
            }
        }
    }
}