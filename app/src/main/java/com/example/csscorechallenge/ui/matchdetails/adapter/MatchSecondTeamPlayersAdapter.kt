package com.example.csscorechallenge.ui.matchdetails.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ItemTeamPlayerRightBinding
import com.example.csscorechallenge.domain.model.PlayerDomain

class MatchSecondTeamPlayersAdapter(
    private val teamPlayerList: List<PlayerDomain>
) : RecyclerView.Adapter<MatchSecondTeamPlayersAdapter.MatchDetailsAdapterViewHolder>() {

    override fun onBindViewHolder(holder: MatchDetailsAdapterViewHolder, position: Int) {
        holder.bind(teamPlayerList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchDetailsAdapterViewHolder {
        val view: View = ItemTeamPlayerRightBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
        return MatchDetailsAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = teamPlayerList.size

    inner class MatchDetailsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val itemTeamPlayerNicknameView: AppCompatTextView =
            view.findViewById(R.id.itemTeamPlayerRightNickname)

        private val itemTeamPlayerNameView: AppCompatTextView =
            view.findViewById(R.id.itemTeamPlayerRightName)

        private val itemTeamPlayerRightImageView: AppCompatImageView =
            view.findViewById(R.id.itemTeamPlayerRightImage)

        fun bind(player: PlayerDomain) {

            itemTeamPlayerNicknameView.apply {
                text = player.nickName
            }

            itemTeamPlayerNameView.apply {
                text = player.firstName
            }

            player.imageUrl?.let { validUrl ->
                Glide.with(itemTeamPlayerRightImageView.context)
                    .load(Uri.parse(validUrl.trim()))
                    .transform(MultiTransformation(FitCenter(), RoundedCorners(8)))
                    .placeholder(R.drawable.bg_team_player_image_rounded)
                    .error(R.drawable.bg_team_player_image_rounded)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemTeamPlayerRightImageView)
            } ?: run {
                itemTeamPlayerRightImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this.itemView.context, R.drawable.bg_team_player_image_rounded
                    )
                )
            }
        }
    }
}