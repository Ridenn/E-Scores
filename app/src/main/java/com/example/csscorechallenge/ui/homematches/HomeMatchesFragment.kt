package com.example.csscorechallenge.ui.homematches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.FragmentHomeMatchesBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.extensions.fadeIn
import com.example.csscorechallenge.extensions.fadeOut
import com.example.csscorechallenge.extensions.gone
import com.example.csscorechallenge.extensions.visible
import com.example.csscorechallenge.ui.homematches.adapter.HomeMatchesAdapter
import com.example.csscorechallenge.ui.homematches.viewmodel.HomeMatchesViewModel
import com.example.csscorechallenge.utils.AnimationConstants
import com.example.csscorechallenge.utils.EndlessRecyclerOnScrollListener
import com.example.csscorechallenge.utils.ToastUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeMatchesFragment : Fragment(),
    HomeMatchesAdapter.HomeMatchesAdapterListClickListener {

    private val homeMatchesViewModel: HomeMatchesViewModel by viewModel()

    private var binding: FragmentHomeMatchesBinding? = null

    private var homeMatchesAdapter: HomeMatchesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val inflateBinding = FragmentHomeMatchesBinding.inflate(inflater, container, false)
        binding = inflateBinding
        return inflateBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSwipeListener()
        setUpViewModelObservers()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding = null
    }

    override fun onMatchClick(match: HomeMatchesDomain) {
        findNavController().navigate(
            HomeMatchesFragmentDirections.toMatchDetails(match)
        )
    }

    private fun setUpSwipeListener() {
        binding?.homeMatchesSwipeRefresh?.setOnRefreshListener {
            homeMatchesAdapter?.updateHomeMatchesList()
            fetchData()
        }
    }

    private fun setUpViewModelObservers() {
        homeMatchesViewModel.showLoadingLiveData.observe(viewLifecycleOwner) { showLoading ->
            if (showLoading) {
                showOrHideLoading(isShowLoading = true)
            } else {
                showOrHideLoading(isShowLoading = false)
            }
        }

        homeMatchesViewModel.getHomeMatchesLiveData.observe(viewLifecycleOwner) { homeMatches ->
            handleGetHomeMatches(homeMatches)
        }

        homeMatchesViewModel.getRunningHomeMatchesLiveData.observe(viewLifecycleOwner) { homeMatches ->
            handleRunningGetHomeMatches(homeMatches)
        }
    }

    private fun handleRunningGetHomeMatches(homeMatchesState: HomeMatchesViewModel.GetRunningHomeMatchesState) {
        when (homeMatchesState) {
            is HomeMatchesViewModel.GetRunningHomeMatchesState.BindData -> {
                homeMatchesViewModel.getHomeMatches(
                    homeMatchesState.page,
                    homeMatchesState.appendData,
                    homeMatchesState.matchList
                )
            }
        }
    }

    private fun handleGetHomeMatches(homeMatchesState: HomeMatchesViewModel.GetHomeMatchesState) {
        when (homeMatchesState) {
            is HomeMatchesViewModel.GetHomeMatchesState.BindData -> {
                bindData(homeMatchesState.matchList)
            }
            is HomeMatchesViewModel.GetHomeMatchesState.AppendData -> {
                homeMatchesAdapter?.append(homeMatchesState.matchList)
            }
            is HomeMatchesViewModel.GetHomeMatchesState.Failure -> {
                Log.w("Error", homeMatchesState.throwable)
                showGenericError()
            }
            is HomeMatchesViewModel.GetHomeMatchesState.NetworkError -> {
                showNetworkError()
            }
            is HomeMatchesViewModel.GetHomeMatchesState.TimeoutError -> {
                showTimeoutError()
            }
        }
    }

    private fun bindData(
        matchList: List<HomeMatchesDomain>
    ) {
        val mutableMatchList = matchList.toMutableList()

        matchList.forEach { match ->
            if (match.opponents?.size == 0) {
                mutableMatchList.remove(match)
            }
        }

        homeMatchesAdapter = HomeMatchesAdapter(mutableMatchList, this)

        binding?.homeMatchesRecyclerView?.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = homeMatchesAdapter

            addOnScrollListener(object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    fetchData(currentPage, true)
                }
            })
        }
    }

    private fun showOrHideLoading(isShowLoading: Boolean) {
        if (isShowLoading) {
//            binding?.homeMatchesProgressBar?.visible()
//            binding?.homeMatchesSwipeRefresh?.isRefreshing = false

            binding?.homeMatchesLoadingLayout?.startShimmer()
            binding?.homeMatchesRecyclerView?.fadeOut(AnimationConstants.SHIMMER.FADE_OUT_DURATION) {
                binding?.homeMatchesLoadingLayout?.fadeIn()
            }
            binding?.homeMatchesSwipeRefresh?.isRefreshing = false
        } else {
            lifecycleScope.launch {
                delay(AnimationConstants.SHIMMER.LOADING_DELAY)
//                binding?.homeMatchesProgressBar?.gone()
//                binding?.homeMatchesRecyclerView?.visible()
                binding?.homeMatchesLoadingLayout?.stopShimmer()
                binding?.homeMatchesLoadingLayout?.fadeOut(AnimationConstants.SHIMMER.FADE_OUT_DURATION) {
                    binding?.homeMatchesRecyclerView?.fadeIn()
                }
            }
        }
    }

    private fun fetchData(page: Int = INITIAL_PAGE, appendData: Boolean = false) {
        homeMatchesViewModel.getRunningHomeMatches(page, appendData)
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
        private const val INITIAL_PAGE = 1
        private const val LOADING_DELAY = 250L
    }
}