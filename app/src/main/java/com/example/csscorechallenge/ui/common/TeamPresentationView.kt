package com.example.csscorechallenge.ui.common

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ViewTeamPresentedBinding
import com.example.csscorechallenge.utils.LoadingUtils

class TeamPresentationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTeamPresentedBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun bind(
        teamImageCover: String? = null,
        teamName: String? = null
    ) {
        setCoverImage(teamImageCover)
        setTeamName(teamName)
    }

    private fun setCoverImage(coverImageUrl: String?) {
        val loadingProgressBar = LoadingUtils.showloadingProgressBar(context)
        loadingProgressBar.start()

        coverImageUrl?.let { validUrl ->
            Glide.with(binding.viewTeamPresentedTeamImageView.context)
                .load(Uri.parse(validUrl.trim()))
                .placeholder(loadingProgressBar)
                .error(R.drawable.ic_error_team)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.viewTeamPresentedTeamImageView)
        } ?: run {
            binding.viewTeamPresentedTeamImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    context, R.drawable.ic_error_team
                )
            )
        }
    }

    private fun setTeamName(teamName: String?) {
        binding.homeMatchesTeamOneLabel.text = teamName
    }
}