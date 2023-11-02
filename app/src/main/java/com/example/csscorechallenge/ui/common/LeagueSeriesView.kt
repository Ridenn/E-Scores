package com.example.csscorechallenge.ui.common

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ViewLeagueSeriesBinding
import com.example.csscorechallenge.utils.LoadingUtils

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
        val loadingProgressBar = LoadingUtils.showloadingProgressBar(context)
        loadingProgressBar.start()

        coverImageUrl?.let { validUrl ->
            Glide.with(binding.viewLeagueSeriesImageView.context)
                .load(Uri.parse(validUrl.trim()))
                .placeholder(loadingProgressBar)
                .error(R.drawable.ic_error_team)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.viewLeagueSeriesImageView)
        } ?: run {
            binding.viewLeagueSeriesImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    context, R.drawable.ic_error_team
                )
            )
        }
    }

    private fun setTeamName(leagueName: String?, serieName: String?) {
        binding.viewLeagueSeriesLabel.text = "$leagueName $serieName"
    }
}