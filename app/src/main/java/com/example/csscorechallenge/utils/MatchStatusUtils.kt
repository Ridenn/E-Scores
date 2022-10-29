package com.example.csscorechallenge.utils

object MatchStatusUtils {

    sealed class MatchStatus {
        object NOT_STARTED : MatchStatus()
        object RUNNING : MatchStatus()
        object FINISHED : MatchStatus()
    }

    fun getMatchStatus(status: String): MatchStatus? {
        return when (status) {
            "not_started" -> MatchStatus.NOT_STARTED
            "running" -> MatchStatus.RUNNING
            "finished" -> MatchStatus.FINISHED
            else -> {
                null
            }
        }
    }
}