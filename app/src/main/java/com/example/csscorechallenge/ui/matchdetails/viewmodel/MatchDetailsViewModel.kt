package com.example.csscorechallenge.ui.matchdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.usecase.GetMatchDetailsUseCase
import com.example.csscorechallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MatchDetailsViewModel constructor(
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase
) : ViewModel() {

    private val _showLoadingLiveData by lazy { SingleLiveEvent<Boolean>() }
    val showLoadingLiveData = _showLoadingLiveData

    sealed class GetFirstTeamDetailsState {
        data class BindData(
            val team: MatchDetailsDomain
        ) : GetFirstTeamDetailsState()

        data class Failure(val throwable: Throwable?) : GetFirstTeamDetailsState()
        object NetworkError : GetFirstTeamDetailsState()
        object TimeoutError : GetFirstTeamDetailsState()
    }

    private val _getFirstTeamDetailsLiveData by lazy { SingleLiveEvent<GetFirstTeamDetailsState>() }
    val getFirstTeamDetailsLiveData: LiveData<GetFirstTeamDetailsState> = _getFirstTeamDetailsLiveData

    fun getFirstTeamDetails(id: Int) {
        _showLoadingLiveData.postValue(true)
        viewModelScope.launch {
            getMatchDetailsUseCase.getMatchDetails(id)
                .collect { result ->
                    handleGetMatchDetailsResult(result)
                }
        }
    }

    private fun handleGetMatchDetailsResult(
        result: Result<MatchDetailsDomain>
    ) {
        val matchDetails = result.getOrNull()
        if (result.isSuccess && matchDetails != null) {
            _getFirstTeamDetailsLiveData.postValue(
                GetFirstTeamDetailsState.BindData(matchDetails)
            )
            _showLoadingLiveData.postValue(false)
        } else {
            _showLoadingLiveData.postValue(false)
            result.exceptionOrNull()?.let { throwable ->
                handleFirstTeamThrowable(throwable)
            }
        }
    }

    sealed class GetSecondTeamDetailsState {
        data class BindData(
            val team: MatchDetailsDomain
        ) : GetSecondTeamDetailsState()

        data class Failure(val throwable: Throwable?) : GetSecondTeamDetailsState()
        object NetworkError : GetSecondTeamDetailsState()
        object TimeoutError : GetSecondTeamDetailsState()
    }

    private val _getSecondTeamDetailsLiveData by lazy { SingleLiveEvent<GetSecondTeamDetailsState>() }
    val getSecondTeamDetailsLiveData: LiveData<GetSecondTeamDetailsState> = _getSecondTeamDetailsLiveData

    fun getSecondTeamDetails(id: Int) {
        _showLoadingLiveData.postValue(true)
        viewModelScope.launch {
            getMatchDetailsUseCase.getMatchDetails(id)
                .collect { result ->
                    handleGetSecondTeamDetailsResult(result)
                }
        }
    }

    private fun handleGetSecondTeamDetailsResult(
        result: Result<MatchDetailsDomain>
    ) {
        val matchDetails = result.getOrNull()
        if (result.isSuccess && matchDetails != null) {
            _getSecondTeamDetailsLiveData.postValue(
                GetSecondTeamDetailsState.BindData(matchDetails)
            )
            _showLoadingLiveData.postValue(false)
        } else {
            _showLoadingLiveData.postValue(false)
            result.exceptionOrNull()?.let { throwable ->
                handleSecondTeamThrowable(throwable)
            }
        }
    }

    private fun handleFirstTeamThrowable(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                _getFirstTeamDetailsLiveData.postValue(
                    GetFirstTeamDetailsState.Failure(throwable)
                )
            }
            is UnknownHostException -> {
                _getFirstTeamDetailsLiveData.postValue(
                    GetFirstTeamDetailsState.NetworkError
                )
            }
            is SocketTimeoutException -> {
                _getFirstTeamDetailsLiveData.postValue(
                    GetFirstTeamDetailsState.TimeoutError
                )
            }
            else -> {
                _getFirstTeamDetailsLiveData.postValue(
                    GetFirstTeamDetailsState.Failure(throwable)
                )
            }
        }
    }

    private fun handleSecondTeamThrowable(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                _getSecondTeamDetailsLiveData.postValue(
                    GetSecondTeamDetailsState.Failure(throwable)
                )
            }
            is UnknownHostException -> {
                _getSecondTeamDetailsLiveData.postValue(
                    GetSecondTeamDetailsState.NetworkError
                )
            }
            is SocketTimeoutException -> {
                _getSecondTeamDetailsLiveData.postValue(
                    GetSecondTeamDetailsState.TimeoutError
                )
            }
            else -> {
                _getSecondTeamDetailsLiveData.postValue(
                    GetSecondTeamDetailsState.Failure(throwable)
                )
            }
        }
    }
}