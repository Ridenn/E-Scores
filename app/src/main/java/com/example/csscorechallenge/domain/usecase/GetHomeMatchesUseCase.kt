package com.example.csscorechallenge.domain.usecase

import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.repository.HomeMatchesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetHomeMatchesUseCase @Inject constructor(
    private val repository: HomeMatchesRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        const val LIMIT = 20
    }

    fun getHomeMatches(currentPage: Int) : Flow<Result<List<HomeMatchesDomain>>> =
        flow {
            val result = repository.getHomeMatches(currentPage, LIMIT)
            emit(Result.success(result))
        }
            .flowOn(defaultDispatcher)
            .catch { throwable -> emit(Result.failure(throwable)) }

    fun getRunningHomeMatches() : Flow<Result<List<HomeMatchesDomain>>> =
        flow {
            val result = repository.getRunningHomeMatches()
            emit(Result.success(result))
        }
            .flowOn(defaultDispatcher)
            .catch { throwable -> emit(Result.failure(throwable)) }
}