package com.example.csscorechallenge.ui.homematches.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ItemMatchReceivedBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.ui.common.LeagueSeriesView
import com.example.csscorechallenge.ui.common.TeamPresentationView

class HomeMatchesAdapter(
    private val matchList: MutableList<HomeMatchesDomain>,
    private val listener: HomeMatchesAdapterListClickListener
) : RecyclerView.Adapter<HomeMatchesAdapter.HomeMatchesAdapterViewHolder>() {

    interface HomeMatchesAdapterListClickListener {
        fun onMatchClick(match: HomeMatchesDomain)
    }

    fun append(newMatchList: List<HomeMatchesDomain>) {
        if (newMatchList.isNotEmpty()) {
            val lastIndex = matchList.lastIndex
            matchList.addAll(newMatchList)
            notifyItemRangeChanged(lastIndex, matchList.size)
        }
    }

    override fun onBindViewHolder(holder: HomeMatchesAdapterViewHolder, position: Int) {
        if (matchList[position].opponents?.size == 2) {
            holder.bind(matchList[position])
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeMatchesAdapterViewHolder {
        val itemView: View = ItemMatchReceivedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
        return HomeMatchesAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int = matchList.size

    inner class HomeMatchesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val homeMatchesItemView: ConstraintLayout =
            view.findViewById(R.id.homeMatchesItem)

        private val homeMatchesDateView: TextView =
            view.findViewById(R.id.homeMatchesDateTextView)

        private val firstTeamView: TeamPresentationView =
            view.findViewById(R.id.homeMatchesFirstTeam)

        private val secondTeamView: TeamPresentationView =
            view.findViewById(R.id.homeMatchesSecondTeam)

        private val leagueSeriesView: LeagueSeriesView =
            view.findViewById(R.id.homeMatchesLeagueSeries)

        fun bind(match: HomeMatchesDomain) {

            // TODO - formatar data de forma inteligente
            homeMatchesDateView.apply {
                text = match.beginAt
            }

            firstTeamView.apply {
                bind(
                    team = match.opponents?.first()
                )
            }

            secondTeamView.apply {
                bind(
                    team = match.opponents?.last()
                )
            }

            leagueSeriesView.apply {
                bind(
                    coverImageUrl = match.league?.imageUrl,
                    leagueName = match.league?.name,
                    serieName = match.serie?.fullName
                )
            }

            homeMatchesItemView.apply {
                setOnClickListener { listener.onMatchClick(match) }
            }
        }

    }

}