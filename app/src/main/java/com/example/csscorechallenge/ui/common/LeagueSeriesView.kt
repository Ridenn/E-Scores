package com.example.csscorechallenge.ui.common

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.csscorechallenge.R
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
        coverImageUrl?.let { validUrl ->
            Glide.with(binding.viewLeagueSeriesImageView.context)
                .load(Uri.parse(validUrl.trim()))
                .error(R.drawable.ic_team_placeholder)
                .into(binding.viewLeagueSeriesImageView)
        } ?: run {
            binding.viewLeagueSeriesImageView.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_team_placeholder, null
                )
            )
        }
    }

    private fun setTeamName(leagueName: String?, serieName: String?) {
        binding.viewLeagueSeriesLabel.text = "$leagueName $serieName"
    }
}