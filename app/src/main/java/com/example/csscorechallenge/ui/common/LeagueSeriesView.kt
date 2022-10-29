package com.example.csscorechallenge.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.csscorechallenge.databinding.ViewLeagueSeriesBinding

class LeagueSeriesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewLeagueSeriesBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun bind(
        coverImageUrl: String? = null,
        leagueName: String? = null,
        serieName: String? = null
    ) {
        setCoverImage(coverImageUrl)
        setTeamName(leagueName, serieName)
    }

    private fun setCoverImage(coverImageUrl: String?) {

    }

    private fun setTeamName(leagueName: String?, serieName: String?) {
        binding.viewLeagueSeriesLabel.text = "$leagueName $serieName"
    }
}