package com.example.csscorechallenge.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ViewTeamOpponentsMatchDetailsBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.extensions.visible
import com.example.csscorechallenge.utils.FormatDateUtils
import com.example.csscorechallenge.utils.MatchStatusUtils

class TeamOpponentsMatchDetailsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTeamOpponentsMatchDetailsBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun bind(
        match: HomeMatchesDomain? = null
    ) {
        binding.teamOponentsFirstTeamView.apply {
            bind(
                teamImageCover = match?.opponents?.first()?.opponent?.imageUrl ?: "",
                teamName = match?.opponents?.first()?.opponent?.name
                    ?: resources.getString(R.string.matches_tba_label)
            )
        }

        binding.teamOponentsSecondTeamView.apply {
            if (match?.opponents?.first() != match?.opponents?.last()) {
                bind(
                    teamImageCover = match?.opponents?.last()?.opponent?.imageUrl ?: "",
                    teamName = match?.opponents?.last()?.opponent?.name
                        ?: resources.getString(R.string.matches_tba_label)
                )
            } else {
                bind(
                    teamImageCover = "",
                    teamName = resources.getString(R.string.matches_tba_label)
                )
            }
        }

        when (match?.status?.let { MatchStatusUtils.getMatchStatus(it) }) {
            is MatchStatusUtils.MatchStatus.RUNNING -> {
                binding.teamOponentsMatchDetailsDateLabel.text = context.getString(R.string.matches_now_label)

                binding.teamOponentsFirstTeamScoreView.visible()
                binding.teamOponentsFirstTeamScoreView.text = match.results?.first()?.score?.toString()

                binding.teamOponentsSecondTeamScoreView.visible()
                binding.teamOponentsSecondTeamScoreView.text = match.results?.last()?.score?.toString()

                binding.teamOponentsMatchDetailsMapInfoLabel.visible()
                binding.teamOponentsMatchDetailsMapInfoLabel.text = context.getString(
                    R.string.match_details_map_info_label,
                    match.results?.size.toString(),
                    match.numberOfGames.toString()
                )
            }
            else -> {
                binding.teamOponentsMatchDetailsDateLabel.text = match?.beginAt?.let {
                    FormatDateUtils.convertToReaderReadableDate(it)
                }
            }
        }
    }
}