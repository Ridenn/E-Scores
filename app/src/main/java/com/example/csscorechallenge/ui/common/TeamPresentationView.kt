package com.example.csscorechallenge.ui.common

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.csscorechallenge.databinding.ViewTeamPresentedBinding
import com.example.csscorechallenge.domain.model.OpponentsDomain

class TeamPresentationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTeamPresentedBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun bind(
        team: OpponentsDomain? = null
    ) {
        setCoverImage(team?.opponent?.imageUrl)
        setTeamName(team?.opponent?.name)
    }

    private fun setCoverImage(coverImageUrl: String?) {
        coverImageUrl?.let { validUrl ->
            Glide.with(binding.viewTeamPresentedTeamImageView.context)
                .load(Uri.parse(validUrl.trim()))
                .into(binding.viewTeamPresentedTeamImageView)
        } ?: run {

        }
    }

    private fun setTeamName(teamName: String?) {
        binding.homeMatchesTeamOneLabel.text = teamName
    }
}