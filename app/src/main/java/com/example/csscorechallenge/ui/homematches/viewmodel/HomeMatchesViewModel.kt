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

    fun getHomeMatches(page: Int, appendData: Boolean = true) {
        currentPage = if (page == INITIAL_PAGE) {
            _showLoadingLiveData.postValue(true)
            INITIAL_PAGE
        } else {
            page
        }
        viewModelScope.launch {
            getHomeMatchesUseCase.getHomeMatches(currentPage)
                .collect { result ->
                    handleGetHomeMatchesResult(result, appendData)
                }
        }
    }

    private fun handleGetHomeMatchesResult(
        result: Result<List<HomeMatchesDomain>>,
        appendData: Boolean
    ) {
        val homeMatches = result.getOrNull()
        if (result.isSuccess && homeMatches != null) {
            if (appendData) {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.AppendData(homeMatches)
                )
            } else {
                _getHomeMatchesLiveData.postValue(
                    GetHomeMatchesState.BindData(homeMatches)
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

}