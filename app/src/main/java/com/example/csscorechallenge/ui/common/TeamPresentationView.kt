package com.example.csscorechallenge.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.csscorechallenge.databinding.ViewTeamPresentedBinding

class TeamPresentationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTeamPresentedBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun bind(
        coverImageUrl: String? = null,
        teamName: String? = null
    ) {
        setCoverImage(coverImageUrl)
    }

    private fun setCoverImage(coverImageUrl: String?) {

    }

    private fun setTeamName(teamName: String?) {
        binding.homeMatchesTeamOneLabel.text = teamName
    }
}