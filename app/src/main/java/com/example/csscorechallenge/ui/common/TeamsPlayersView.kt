package com.example.csscorechallenge.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csscorechallenge.databinding.ViewTeamsPlayersBinding
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.ui.matchdetails.adapter.MatchFirstTeamPlayersAdapter
import com.example.csscorechallenge.ui.matchdetails.adapter.MatchSecondTeamPlayersAdapter

class TeamsPlayersView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTeamsPlayersBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var firstTeamPlayersAdapter: MatchFirstTeamPlayersAdapter? = null

    private var secondTeamPlayersAdapter: MatchSecondTeamPlayersAdapter? = null

    fun bindFirstTeamData(
        team: MatchDetailsDomain,
        context: Context
    ) {
        val firstTeamPlayersAdapter = team.players?.let { teamPlayerList -> MatchFirstTeamPlayersAdapter(teamPlayerList.toMutableList(), context) }

        binding.viewFirstTeamsPlayersReciclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = firstTeamPlayersAdapter
        }
    }

    fun updateTeamsList() {
        firstTeamPlayersAdapter?.updateTeamPlayerList()
        secondTeamPlayersAdapter?.updateTeamPlayerList()
    }

    fun bindSecondData(
        team: MatchDetailsDomain,
        context: Context
    ) {
        val secondTeamPlayersAdapter = team.players?.let { teamPlayerList -> MatchSecondTeamPlayersAdapter(teamPlayerList.toMutableList(), context) }

        binding.viewSecondTeamsPlayersReciclerView.apply {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = secondTeamPlayersAdapter
        }
    }
}