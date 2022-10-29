package com.example.csscorechallenge.ui.common

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ViewTeamPresentedBinding

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
        coverImageUrl?.let { validUrl ->
            Glide.with(binding.viewTeamPresentedTeamImageView.context)
                .load(Uri.parse(validUrl.trim()))
                .error(R.drawable.ic_team_placeholder)
                .into(binding.viewTeamPresentedTeamImageView)
        } ?: run {
            binding.viewTeamPresentedTeamImageView.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_team_placeholder, null
                )
            )
        }
    }

    private fun setTeamName(teamName: String?) {
        binding.homeMatchesTeamOneLabel.text = teamName
    }
}