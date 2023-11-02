package com.example.csscorechallenge.ui.matchdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.FragmentMatchDetailsBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.extensions.fadeIn
import com.example.csscorechallenge.extensions.fadeOut
import com.example.csscorechallenge.ui.matchdetails.adapter.MatchFirstTeamPlayersAdapter
import com.example.csscorechallenge.ui.matchdetails.adapter.MatchSecondTeamPlayersAdapter
import com.example.csscorechallenge.ui.matchdetails.viewmodel.MatchDetailsViewModel
import com.example.csscorechallenge.utils.AnimationConstants
import com.example.csscorechallenge.utils.FormatDateUtils
import com.example.csscorechallenge.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        setUpActionBar(match.league?.name, match.serie?.fullName)
        setUpViewModelObservers()
        fetchData(match)
        bindMatch(match)
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

    private fun showOrHideLoading(isShowLoading: Boolean) {
        if (isShowLoading) {
            binding?.matchDetailsLoadingLayout?.startShimmer()
        } else {
            lifecycleScope.launch {
                delay(AnimationConstants.SHIMMER.LOADING_DELAY)
                binding?.matchDetailsLoadingLayout?.stopShimmer()
                binding?.matchDetailsLoadingLayout?.fadeOut(AnimationConstants.SHIMMER.FADE_OUT_DURATION) {
                    binding?.homeMatchesDetails?.fadeIn()
                    binding?.homeMatchesTeamsVersus?.fadeIn()
                }
            }
        }
    }

    private fun fetchData(match: HomeMatchesDomain) {
        if (match.opponents?.size == 2) {
            match.opponents.first().opponent?.id?.let { id ->
                fetchFirstTeamData(id = id)
            }
            match.opponents.last().opponent?.id?.let { id ->
                fetchSecondTeamData(id = id)
            }
        } else if (match.opponents?.size == 1) {
            match.opponents.first().opponent?.id?.let { id ->
                fetchFirstTeamData(id = id)
            }
        }
    }

    private fun fetchFirstTeamData(id: Int) {
        matchDetailsViewModel.getFirstTeamDetails(id = id)
    }

    private fun fetchSecondTeamData(id: Int) {
        matchDetailsViewModel.getSecondTeamDetails(id = id)
    }

    private fun handleGetTeamDetails(matchDetailsState: MatchDetailsViewModel.GetFirstTeamDetailsState) {
        when (matchDetailsState) {
            is MatchDetailsViewModel.GetFirstTeamDetailsState.BindData -> {
                bindFirstData(matchDetailsState.team)
            }
            is MatchDetailsViewModel.GetFirstTeamDetailsState.Failure -> {
                Log.w("Error", matchDetailsState.throwable)
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
                Log.w("Error", matchDetailsState.throwable)
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

    private fun setUpActionBar(leagueName: String?, serieName: String?) {
        (activity as AppCompatActivity).supportActionBar?.title = "$leagueName $serieName"
    }

    private fun bindMatch(match: HomeMatchesDomain) {
        binding?.matchDetailsTeamOpponents?.bind(match)

        binding?.matchDetailsDateLabel?.text =
            match.beginAt?.let {
                FormatDateUtils.convertToReaderReadableDate(it)
            }
    }

    private fun bindFirstData(team: MatchDetailsDomain) {
        if (team.players?.size != 0) {
            val teamAdapter = team.players?.let { teamPlayerList -> MatchFirstTeamPlayersAdapter(teamPlayerList, requireContext()) }

            binding?.matchDetailsFirstTeamPlayersReciclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = teamAdapter
            }
        } else {
            lifecycleScope.launch {
                delay(LOADING_DELAY)
                showTeamPlayerError(team.name)
            }
        }
    }

    private fun bindSecondData(team: MatchDetailsDomain) {
        if (team.players?.size != 0) {
            val teamAdapter = team.players?.let { teamPlayerList -> MatchSecondTeamPlayersAdapter(teamPlayerList, requireContext()) }

            binding?.matchDetailsSecondTeamPlayersReciclerView?.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = teamAdapter
            }
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