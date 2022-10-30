package com.example.csscorechallenge.ui.matchdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
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

    sealed class GetMatchDetailsState {
        data class BindData(
            val team: MatchDetailsDomain,
            val isFirstTeam: Boolean,
            val match: HomeMatchesDomain?
        ) : GetMatchDetailsState()

        data class Failure(val throwable: Throwable?) : GetMatchDetailsState()
        object NetworkError : GetMatchDetailsState()
        object TimeoutError : GetMatchDetailsState()
    }

    private val _getMatchDetailsLiveData by lazy { SingleLiveEvent<GetMatchDetailsState>() }
    val getMatchDetailsLiveData: LiveData<GetMatchDetailsState> = _getMatchDetailsLiveData

    fun getTeamDetailsTeam(id: Int, isFirstTeam: Boolean, match: HomeMatchesDomain? = null) {
        _showLoadingLiveData.postValue(true)
        viewModelScope.launch {
            getMatchDetailsUseCase.getMatchDetails(id)
                .collect { result ->
                    handleGetMatchDetailsResult(result, isFirstTeam, match)
                }
        }
    }

    private fun handleGetMatchDetailsResult(
        result: Result<MatchDetailsDomain>,
        isFirstTeam: Boolean,
        match: HomeMatchesDomain?
    ) {
        val matchDetails = result.getOrNull()
        if (result.isSuccess && matchDetails != null) {
            _getMatchDetailsLiveData.postValue(
                GetMatchDetailsState.BindData(matchDetails, isFirstTeam, match)
            )
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
                _getMatchDetailsLiveData.postValue(
                    GetMatchDetailsState.Failure(throwable)
                )
            }
            is UnknownHostException -> {
                _getMatchDetailsLiveData.postValue(
                    GetMatchDetailsState.NetworkError
                )
            }
            is SocketTimeoutException -> {
                _getMatchDetailsLiveData.postValue(
                    GetMatchDetailsState.TimeoutError
                )
            }
            else -> {
                _getMatchDetailsLiveData.postValue(
                    GetMatchDetailsState.Failure(throwable)
                )
            }
        }
    }
}