package com.example.csscorechallenge.ui.matchdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.usecase.GetMatchDetailsUseCase
import com.example.csscorechallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MatchDetailsViewModel constructor(
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase
) : ViewModel() {

    private val _showLoadingLiveData by lazy { SingleLiveEvent<Boolean>() }
    val showLoadingLiveData = _showLoadingLiveData

    sealed class GetMatchDetailsState {
        data class BindData(
            val team: MatchDetailsDomain,
            val isFirstTeam: Boolean
        ) : GetMatchDetailsState()

        data class Failure(val throwable: Throwable?) : GetMatchDetailsState()
        object NetworkError : GetMatchDetailsState()
    }

    private val _getMatchDetailsLiveData by lazy { SingleLiveEvent<GetMatchDetailsState>() }
    val getMatchDetailsLiveData: LiveData<GetMatchDetailsState> = _getMatchDetailsLiveData

    fun getTeamDetailsTeam(id: Int, isFirstTeam: Boolean) {
        _showLoadingLiveData.postValue(true)
        viewModelScope.launch {
            getMatchDetailsUseCase.getMatchDetails(id)
                .collect { result ->
                    handleGetMatchDetailsResult(result, isFirstTeam)
                }
        }
    }

    private fun handleGetMatchDetailsResult(
        result: Result<MatchDetailsDomain>,
        isFirstTeam: Boolean
    ) {
        val matchDetails = result.getOrNull()
        if (result.isSuccess && matchDetails != null) {
            _getMatchDetailsLiveData.postValue(
                GetMatchDetailsState.BindData(matchDetails, isFirstTeam)
            )
            _showLoadingLiveData.postValue(false)
        } else {
            _showLoadingLiveData.postValue(false)
            result.exceptionOrNull()?.let { throwable ->
                if (throwable is UnknownHostException) {
                    _getMatchDetailsLiveData.postValue(
                        GetMatchDetailsState.NetworkError
                    )
                } else {
                    _getMatchDetailsLiveData.postValue(
                        GetMatchDetailsState.Failure(result.exceptionOrNull())
                    )
                }
            }
        }
    }
}