package com.example.csscorechallenge.ui.matchdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.usecase.GetMatchDetailsUseCase
import com.example.csscorechallenge.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MatchDetailsViewModel constructor(
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase
) : ViewModel() {

    private val _showLoadingLiveData by lazy { MutableLiveData<Boolean>() }
    val showLoadingLiveData = _showLoadingLiveData

    fun fetchTeamsData(match: HomeMatchesDomain) {
        val firstTeamId = match.opponents?.first()?.opponent?.id
        val secondTeamId = match.opponents?.last()?.opponent?.id

        if (match.opponents?.size == 2 &&
            firstTeamId != null &&
            secondTeamId != null
            )
        {
            getFirstTeamDetails(firstTeamId, isBothTeams = true)
            getSecondTeamDetails(secondTeamId)
        } else {
            if (firstTeamId != null) {
                getFirstTeamDetails(firstTeamId)
            }
        }
    }

    sealed class GetFirstTeamDetailsState {
        data class BindData(
            val team: MatchDetailsDomain
        ) : GetFirstTeamDetailsState()

        data class Failure(val throwable: Throwable?) : GetFirstTeamDetailsState()
        object NetworkError : GetFirstTeamDetailsState()
        object TimeoutError : GetFirstTeamDetailsState()
    }

    private val _getFirstTeamDetailsLiveData by lazy { MutableLiveData<GetFirstTeamDetailsState>() }
    val getFirstTeamDetailsLiveData = _getFirstTeamDetailsLiveData

    fun getFirstTeamDetails(id: Int, isBothTeams: Boolean = false) {
        _showLoadingLiveData.postValue(true)
        viewModelScope.launch {
            getMatchDetailsUseCase.getMatchDetails(id)
                .collect { result ->
                    handleGetMatchDetailsResult(result, isBothTeams)
                }
        }
    }

    private fun handleGetMatchDetailsResult(
        result: Result<MatchDetailsDomain>,
        isBothTeams: Boolean
    ) {
        val matchDetails = result.getOrNull()
        if (result.isSuccess && matchDetails != null) {
            _getFirstTeamDetailsLiveData.postValue(
                GetFirstTeamDetailsState.BindData(matchDetails)
            )
        } else {
            result.exceptionOrNull()?.let { throwable ->
                handleFirstTeamThrowable(throwable)
            }
        }
        if (!isBothTeams) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(2000)
                _showLoadingLiveData.postValue(false)
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
        } else {
            result.exceptionOrNull()?.let { throwable ->
                handleSecondTeamThrowable(throwable)
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            _showLoadingLiveData.postValue(false)
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