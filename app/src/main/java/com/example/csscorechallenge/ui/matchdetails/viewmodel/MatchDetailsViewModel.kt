package com.example.csscorechallenge.ui.matchdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.usecase.GetMatchDetailsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MatchDetailsViewModel (
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase
) : ViewModel() {

    private val _showLoadingLiveData by lazy { MutableLiveData<Boolean>() }
    val showLoadingLiveData = _showLoadingLiveData

    fun fetchTeamsDetails(match: HomeMatchesDomain) {
        val firstTeamId = match.opponents?.first()?.opponent?.id
        val secondTeamId = match.opponents?.last()?.opponent?.id

        viewModelScope.launch {
            firstTeamId?.let {
                _showLoadingLiveData.postValue(true)

                if (match.opponents.size == 2 && secondTeamId != null) {
                    getMatchDetailsUseCase.getMatchDetails(firstTeamId)
                        .zip(getMatchDetailsUseCase.getMatchDetails(secondTeamId)) { firstResult, secondResult ->
                            Pair(firstResult, secondResult)
                        } .collect { pair ->
                            handleGetMatchDetailsResult(pair.first, pair.second)
                        }
                } else {
                    getMatchDetailsUseCase.getMatchDetails(firstTeamId)
                        .collect { result -> handleGetMatchDetailsResult(result) }
                }
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

    private fun handleGetMatchDetailsResult(
        firstTeamResult: Result<MatchDetailsDomain>,
        secondTeamResult: Result<MatchDetailsDomain>? = null
    ) {
        val firstTeamMatchDetails = firstTeamResult.getOrNull()
        val secondTeamMatchDetails = secondTeamResult?.getOrNull()

        firstTeamMatchDetails?.let { postTeamMatchDetails(firstTeamResult, it, true) }
        secondTeamMatchDetails?.let { postTeamMatchDetails(secondTeamResult, it) }

        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            _showLoadingLiveData.postValue(false)
        }
    }

    private fun postTeamMatchDetails(
        teamResult: Result<MatchDetailsDomain>,
        teamMatchDetails: MatchDetailsDomain,
        isFirstTeam: Boolean = false
    ) {
        if (teamResult.isSuccess) {
            if (isFirstTeam) {
                _getFirstTeamDetailsLiveData.postValue(GetFirstTeamDetailsState.BindData(teamMatchDetails))
            } else {
                _getSecondTeamDetailsLiveData.postValue(GetSecondTeamDetailsState.BindData(teamMatchDetails))
            }
        } else {
            teamResult.exceptionOrNull()?.let { throwable ->
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

    private val _getSecondTeamDetailsLiveData by lazy { MutableLiveData<GetSecondTeamDetailsState>() }
    val getSecondTeamDetailsLiveData: LiveData<GetSecondTeamDetailsState> = _getSecondTeamDetailsLiveData

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