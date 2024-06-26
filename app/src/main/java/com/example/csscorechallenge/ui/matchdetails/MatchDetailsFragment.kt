package com.example.csscorechallenge.ui.matchdetails

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.FragmentMatchDetailsBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.extensions.fadeIn
import com.example.csscorechallenge.extensions.fadeOut
import com.example.csscorechallenge.extensions.gone
import com.example.csscorechallenge.ui.matchdetails.viewmodel.MatchDetailsViewModel
import com.example.csscorechallenge.utils.AnimationConstants
import com.example.csscorechallenge.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MatchDetailsFragment : Fragment() {

    private val matchDetailsViewModel: MatchDetailsViewModel by viewModel()

    private var binding: FragmentMatchDetailsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflateBinding = FragmentMatchDetailsBinding.inflate(inflater, container, false)
        binding = inflateBinding
        return inflateBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val match: HomeMatchesDomain =
            MatchDetailsFragmentArgs.fromBundle(requireArguments()).selectedMatch

//        setupToolbar(match.name, toolbarTitle)
        setupToolbar(" ${match.name} (${match.league?.name} ${match.serie?.fullName})", getToolbarTitle())
        setUpSwipeListener(match)
        setUpViewModelObservers()
        fetchData(match)
        bindMatch(match)
    }

    private fun getToolbarTitle(): TextView {
        val toolbarTitle = TextView(requireContext())
        toolbarTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        toolbarTitle.isFocusable = true
        with(context) {
            resources.getColor(R.color.white, this?.theme)
                .let { toolbarTitle.setTextColor(it) }
        }
        toolbarTitle.isFocusableInTouchMode = true
        toolbarTitle.marqueeRepeatLimit = -1
        toolbarTitle.isHorizontalScrollBarEnabled = true
        toolbarTitle.isSingleLine = true
        toolbarTitle.textSize = 20f
        return toolbarTitle
    }

    private fun setUpViewModelObservers() {
        matchDetailsViewModel.showLoadingLiveData.observe(viewLifecycleOwner) { showLoading ->
            showOrHideLoading(isShowLoading = showLoading)
        }

        matchDetailsViewModel.getFirstTeamDetailsLiveData.observe(viewLifecycleOwner) { teamDetails ->
            handleGetTeamDetails(teamDetails)
        }

        matchDetailsViewModel.getSecondTeamDetailsLiveData.observe(viewLifecycleOwner) { teamDetails ->
            handleGetSecondTeamDetails(teamDetails)
        }
    }

    private fun setUpSwipeListener(match: HomeMatchesDomain) {
        binding?.matchDetailsSwipeRefresh?.setOnRefreshListener {
            binding?.matchDetailsTeamsPlayers?.updateTeamsList()
            fetchData(match)
        }
    }

    private fun showOrHideLoading(isShowLoading: Boolean) {
        if (isShowLoading) {
            binding?.matchDetailsLoadingLayout?.startShimmer()

            binding?.matchDetailsLoadingLayout?.fadeIn(AnimationConstants.SHIMMER.FADE_OUT_DURATION)
            binding?.matchDetailsDetails?.fadeOut(AnimationConstants.SHIMMER.FADE_OUT_DURATION)
            binding?.homeMatchesTeamsVersus?.fadeOut(AnimationConstants.SHIMMER.FADE_OUT_DURATION)

            binding?.matchDetailsSwipeRefresh?.isRefreshing = false
        } else {
            lifecycleScope.launch {
                delay(AnimationConstants.SHIMMER.LOADING_DELAY)
                binding?.matchDetailsLoadingLayout?.stopShimmer()

                binding?.matchDetailsLoadingLayout?.fadeOut(AnimationConstants.SHIMMER.FADE_OUT_DURATION) {
                    binding?.matchDetailsDetails?.fadeIn(AnimationConstants.SHIMMER.FADE_OUT_DURATION)
                    binding?.homeMatchesTeamsVersus?.fadeIn(AnimationConstants.SHIMMER.FADE_OUT_DURATION)
                }

                binding?.matchDetailsLoadingLayout?.gone()
            }
        }
    }

    private fun fetchData(match: HomeMatchesDomain) {
        matchDetailsViewModel.fetchTeamsDetails(match)
    }

    private fun handleGetTeamDetails(matchDetailsState: MatchDetailsViewModel.GetFirstTeamDetailsState) {
        when (matchDetailsState) {
            is MatchDetailsViewModel.GetFirstTeamDetailsState.BindData -> {
                bindFirstData(matchDetailsState.team)
            }
            is MatchDetailsViewModel.GetFirstTeamDetailsState.Failure -> {
                Timber.w("Error: ${matchDetailsState.throwable}")
                showGenericError()
            }
            is MatchDetailsViewModel.GetFirstTeamDetailsState.NetworkError -> {
                showNetworkError()
            }
            is MatchDetailsViewModel.GetFirstTeamDetailsState.TimeoutError -> {
                showTimeoutError()
            }
        }
    }

    private fun handleGetSecondTeamDetails(matchDetailsState: MatchDetailsViewModel.GetSecondTeamDetailsState) {
        when (matchDetailsState) {
            is MatchDetailsViewModel.GetSecondTeamDetailsState.BindData -> {
                bindSecondData(matchDetailsState.team)
            }
            is MatchDetailsViewModel.GetSecondTeamDetailsState.Failure -> {
                Timber.w("Error: ${matchDetailsState.throwable}")
                showGenericError()
            }
            is MatchDetailsViewModel.GetSecondTeamDetailsState.NetworkError -> {
                showNetworkError()
            }
            is MatchDetailsViewModel.GetSecondTeamDetailsState.TimeoutError -> {
                showTimeoutError()
            }
        }
    }

    private fun setupToolbarWithLeagueAndSerie(leagueName: String?, serieName: String?) {
        (activity as AppCompatActivity).supportActionBar?.title = "$leagueName $serieName"
    }

    private fun setupToolbar(gameName: String?, toolbarTitle: TextView) {
        (activity as AppCompatActivity).supportActionBar?.customView = toolbarTitle
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowCustomEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = null
        toolbarTitle.text = "$gameName"

        toolbarTitle.requestFocus()

        val handler = Handler(Looper.getMainLooper())
        handler.post {
            toolbarTitle.isSelected = true
        }
    }

    private fun bindMatch(match: HomeMatchesDomain) {
        binding?.matchDetailsTeamOpponents?.bind(match)
    }

    private fun bindFirstData(team: MatchDetailsDomain) {
        if (team.players?.size != 0) {
            binding?.matchDetailsTeamsPlayers?.bindFirstTeamData(team, requireContext())
        } else {
            lifecycleScope.launch {
                delay(LOADING_DELAY)
                showTeamPlayerError(team.name)
            }
        }
    }

    private fun bindSecondData(team: MatchDetailsDomain) {
        if (team.players?.size != 0) {
            binding?.matchDetailsTeamsPlayers?.bindSecondData(team, requireContext())
        } else {
            lifecycleScope.launch {
                delay(LOADING_DELAY)
                showTeamPlayerError(team.name)
            }
        }
    }

    private fun showTeamPlayerError(teamName: String?) {
        ToastUtils.showToastMessage(
            getString(R.string.match_details_players_error_label) + teamName,
            requireContext()
        )
    }

    private fun showGenericError() {
        ToastUtils.showToastMessage(
            getString(R.string.generic_error_label),
            requireContext()
        )
    }

    private fun showTimeoutError() {
        ToastUtils.showToastMessage(
            getString(R.string.timeout_error_label),
            requireContext()
        )
    }

    private fun showNetworkError() {
        ToastUtils.showToastMessage(
            getString(R.string.network_error_label),
            requireContext()
        )
    }

    companion object {
        private const val LOADING_DELAY = 1000L
    }
}