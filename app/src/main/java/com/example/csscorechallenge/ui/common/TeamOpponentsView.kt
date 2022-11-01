package com.example.csscorechallenge.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ViewTeamOponentsBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain

class TeamOpponentsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTeamOponentsBinding.inflate(
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
    }
}