package com.example.csscorechallenge.ui.matchdetails.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.ItemTeamPlayerLeftBinding
import com.example.csscorechallenge.domain.model.PlayerDomain
import com.example.csscorechallenge.utils.LoadingUtils

class MatchFirstTeamPlayersAdapter(
    private val teamPlayerList: MutableList<PlayerDomain>,
    private val context: Context
) : RecyclerView.Adapter<MatchFirstTeamPlayersAdapter.MatchDetailsAdapterViewHolder>() {

    override fun onBindViewHolder(holder: MatchDetailsAdapterViewHolder, position: Int) {
        holder.bind(teamPlayerList[position], context)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchDetailsAdapterViewHolder {
        val itemView: View = ItemTeamPlayerLeftBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).root
        return MatchDetailsAdapterViewHolder(itemView)
    }

    override fun getItemCount(): Int = teamPlayerList.size

    fun updateTeamPlayerList() {
        teamPlayerList.clear()
        notifyDataSetChanged()
    }

    inner class MatchDetailsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val itemTeamPlayerNicknameView: AppCompatTextView =
            view.findViewById(R.id.itemTeamPlayerLeftNickname)

        private val itemTeamPlayerNameView: AppCompatTextView =
            view.findViewById(R.id.itemTeamPlayerLeftName)

        private val itemTeamPlayerLeftImageView: AppCompatImageView =
            view.findViewById(R.id.itemTeamPlayerLeftImage)

        fun bind(player: PlayerDomain, context: Context) {

            itemTeamPlayerNicknameView.apply {
                text = player.nickName
            }

            itemTeamPlayerNameView.apply {
                text = player.firstName
            }

            val loadingProgressBar = LoadingUtils.showloadingProgressBar(context)
            loadingProgressBar.start()

            val errorPlayer = LoadingUtils.getErrorPlayer()

            player.imageUrl?.let { validUrl ->
                Glide.with(itemTeamPlayerLeftImageView.context)
                    .load(Uri.parse(validUrl.trim()))
                    .transform(MultiTransformation(FitCenter(), RoundedCorners(8)))
                    .placeholder(loadingProgressBar)
                    .error(errorPlayer)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemTeamPlayerLeftImageView)
            } ?: run {
                itemTeamPlayerLeftImageView.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this.itemView.context, errorPlayer
                    )
                )
            }
        }
    }
}