package com.example.csscorechallenge.ui.homematches.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.usecase.GetHomeMatchesUseCase
import com.example.csscorechallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch
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
        data class BindData(val matchList: List<HomeMatchesDomain>) : GetHomeMatchesState()
        data class AppendData(val matchList: List<HomeMatchesDomain>) : GetHomeMatchesState()
        data class Failure(val throwable: Throwable?) : GetHomeMatchesState()
        object NetworkError : GetHomeMatchesState()
    }

    private val _getHomeMatchesLiveData by lazy { SingleLiveEvent<GetHomeMatchesState>() }
    val getHomeMatchesLiveData: LiveData<GetHomeMatchesState> = _getHomeMatchesLiveData

//    fun getHomeMatches(page: Int, appendData: Boolean = true) {
//        _showLoadingLiveData.postValue(true)
//        currentPage = if (page == INITIAL_PAGE) {
//            INITIAL_PAGE
//        } else {
//            page
//        }
//        viewModelScope.launch {
//            getHomeMatchesUseCase.getHomeMatches(currentPage)
//                .collect { result ->
//                    handleGetHomeMatchesResult(result, appendData)
//                }
//        }
//    }

    fun getHomeMatches(page: Int, appendData: Boolean = true, matchList: List<HomeMatchesDomain>? = null) {
        _showLoadingLiveData.postValue(true)
        currentPage = if (page == INITIAL_PAGE) {
            INITIAL_PAGE
        } else {
            page
        }
        viewModelScope.launch {
            getHomeMatchesUseCase.getHomeMatches(currentPage)
                .collect { result ->
                    handleGetHomeMatchesResult(result, appendData, matchList)
                }
        }
    }

//    private fun handleGetHomeMatchesResult(
//        result: Result<List<HomeMatchesDomain>>,
//        appendData: Boolean
//    ) {
//        val homeMatches = result.getOrNull()
//        if (result.isSuccess && homeMatches != null) {
//            if (appendData) {
//                _getHomeMatchesLiveData.postValue(
//                    GetHomeMatchesState.AppendData(homeMatches)
//                )
//            } else {
//                _getHomeMatchesLiveData.postValue(
//                    GetHomeMatchesState.BindData(homeMatches)
//                )
//            }
//            _showLoadingLiveData.postValue(false)
//        } else {
//            _showLoadingLiveData.postValue(false)
//            result.exceptionOrNull()?.let { throwable ->
//                if (throwable is UnknownHostException) {
//                    _getHomeMatchesLiveData.postValue(
//                        GetHomeMatchesState.NetworkError
//                    )
//                } else {
//                    _getHomeMatchesLiveData.postValue(
//                        GetHomeMatchesState.Failure(result.exceptionOrNull())
//                    )
//                }
//            }
//        }
//    }

    private fun handleGetHomeMatchesResult(
        result: Result<List<HomeMatchesDomain>>,
        appendData: Boolean,
        matchList: List<HomeMatchesDomain>? = null
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
                    mergedMatchesList.let { GetHomeMatchesState.BindData(it) }
                )
            }
            _showLoadingLiveData.postValue(false)
        } else {
            _showLoadingLiveData.postValue(false)
            result.exceptionOrNull()?.let { throwable ->
                if (throwable is UnknownHostException) {
                    _getHomeMatchesLiveData.postValue(
                        GetHomeMatchesState.NetworkError
                    )
                } else {
                    _getHomeMatchesLiveData.postValue(
                        GetHomeMatchesState.Failure(result.exceptionOrNull())
                    )
                }
            }
        }
    }

    sealed class GetRunningHomeMatchesState {
        data class BindData(
            val matchList: List<HomeMatchesDomain>,
            val page: Int,
            val appendData: Boolean
        ) : GetRunningHomeMatchesState()
    }

    private val _getRunningHomeMatchesLiveData by lazy { SingleLiveEvent<GetRunningHomeMatchesState>() }
    val getRunningHomeMatchesLiveData: LiveData<GetRunningHomeMatchesState> = _getRunningHomeMatchesLiveData

    fun getRunningHomeMatches(page: Int, appendData: Boolean) {
        _showLoadingLiveData.postValue(true)
        viewModelScope.launch {
            getHomeMatchesUseCase.getRunningHomeMatches()
                .collect { result ->
                    handleGetRunningHomeMatchesResult(
                        result,
                        page,
                        appendData
                    )
                }
        }
    }

    private fun handleGetRunningHomeMatchesResult(
        result: Result<List<HomeMatchesDomain>>,
        page: Int,
        appendData: Boolean
    ) {
        val homeMatches = result.getOrNull()
        _getRunningHomeMatchesLiveData.postValue(
            homeMatches?.let { GetRunningHomeMatchesState.BindData(
                it,
                page,
                appendData
            ) }
        )
    }
}