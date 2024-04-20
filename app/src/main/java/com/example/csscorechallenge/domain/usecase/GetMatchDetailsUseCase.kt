package com.example.csscorechallenge.domain.usecase

import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.domain.repository.MatchDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GetMatchDetailsUseCase @Inject constructor(
    private val repository: MatchDetailsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getMatchDetails(id: Int) : Flow<Result<MatchDetailsDomain>> =
        flow {
            val result = repository.getMatchDetails(id)
            emit(Result.success(result))
        }.flowOn(defaultDispatcher)
            .catch { throwable ->
                Timber.e("Error when fetching match details: $id", throwable)
                emit(Result.failure(throwable))
            }
}