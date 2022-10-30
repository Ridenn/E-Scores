package com.example.csscorechallenge.ui.matchdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.FragmentMatchDetailsBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.domain.model.MatchDetailsDomain
import com.example.csscorechallenge.extensions.gone
import com.example.csscorechallenge.extensions.visible
import com.example.csscorechallenge.ui.matchdetails.adapter.MatchFirstTeamPlayersAdapter
import com.example.csscorechallenge.ui.matchdetails.adapter.MatchSecondTeamPlayersAdapter
import com.example.csscorechallenge.ui.matchdetails.viewmodel.MatchDetailsViewModel
import com.example.csscorechallenge.utils.FormatDateUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.logging.Logger


class MatchDetailsFragment : Fragment() {

    private val matchDetailsViewModel: MatchDetailsViewModel by viewModel()

    private var binding: FragmentMatchDetailsBinding? = null

    private var matchFirstTeamPlayersAdapter: MatchFirstTeamPlayersAdapter? = null
    private var matchSecondTeamPlayersAdapter: MatchSecondTeamPlayersAdapter? = null

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
            if (showLoading) {
                showOrHideLoading(isShow = true)
            } else {
                showOrHideLoading(isShow = false)
            }
        }

        matchDetailsViewModel.getMatchDetailsLiveData.observe(viewLifecycleOwner) { teamDetails ->
            handleGetTeamDetails(teamDetails)
        }
    }

    private fun handleGetTeamDetails(matchDetailsState: MatchDetailsViewModel.GetMatchDetailsState?) {
        when (matchDetailsState) {
            is MatchDetailsViewModel.GetMatchDetailsState.BindData -> {
                if (matchDetailsState.isFirstTeam) {
                    bindFirstData(matchDetailsState.team)
                } else {
                    bindSecondData(matchDetailsState.team)
                }
            }
            is MatchDetailsViewModel.GetMatchDetailsState.Failure -> {
                Log.w("Error", matchDetailsState.throwable)
                Toast.makeText(requireContext(), getString(R.string.generic_error_label), Toast.LENGTH_LONG).show()
            }
            is MatchDetailsViewModel.GetMatchDetailsState.NetworkError -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.network_error_label),
                    Toast.LENGTH_LONG
                ).show()
            }
            is MatchDetailsViewModel.GetMatchDetailsState.TimeoutError -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.timeout_error_label),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {}
        }
    }

    private fun showOrHideLoading(isShow: Boolean) {
        if (isShow) {
            binding?.matchDetailsProgressBar?.visible()
        } else {
            binding?.matchDetailsProgressBar?.gone()
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
        matchDetailsViewModel.getTeamDetailsTeam(
            id = id,
            isFirstTeam = true,
        )
    }

    private fun fetchSecondTeamData(id: Int) {
        matchDetailsViewModel.getTeamDetailsTeam(
            id = id,
            isFirstTeam = false,
        )
    }

    private fun setUpActionBar(leagueName: String?, serieName: String?) {
        (activity as AppCompatActivity).supportActionBar?.title = "$leagueName $serieName"
        (activity as AppCompatActivity).supportActionBar?.setElevation(0F)
    }

    private fun bindMatch(match: HomeMatchesDomain) {
        binding?.matchDetailsFirstTeam?.bind(
            teamImageCover = match.opponents?.first()?.opponent?.imageUrl ?: "",
            teamName = match.opponents?.first()?.opponent?.name
                ?: resources.getString(R.string.matches_tba_label)
        )

        binding?.matchDetailsSecondTeam?.bind(
            teamImageCover = match.opponents?.last()?.opponent?.imageUrl ?: "",
            teamName = match.opponents?.last()?.opponent?.name
                ?: resources.getString(R.string.matches_tba_label)
        )

        binding?.matchDetailsDateLabel?.text =
            match.beginAt?.let {
                FormatDateUtils.convertToReaderReadableDate(it)
            }
    }

    private fun bindFirstData(team: MatchDetailsDomain) {
        matchFirstTeamPlayersAdapter = team.players?.let { playerList ->
            MatchFirstTeamPlayersAdapter(playerList)
        }

        binding?.matchDetailsFirstTeamPlayersReciclerView?.apply {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = matchFirstTeamPlayersAdapter
            (binding?.matchDetailsFirstTeamPlayersReciclerView?.itemAnimator as SimpleItemAnimator)
                .supportsChangeAnimations = false
        }
    }

    private fun bindSecondData(team: MatchDetailsDomain) {
        matchSecondTeamPlayersAdapter = team.players?.let { playerList ->
            MatchSecondTeamPlayersAdapter(playerList)
        }

        binding?.matchDetailsSecondTeamPlayersReciclerView?.apply {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = matchSecondTeamPlayersAdapter
            (binding?.matchDetailsSecondTeamPlayersReciclerView?.itemAnimator as SimpleItemAnimator)
                .supportsChangeAnimations = false
        }
    }
}