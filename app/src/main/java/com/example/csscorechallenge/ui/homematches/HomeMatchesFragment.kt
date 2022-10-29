package com.example.csscorechallenge.ui.homematches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.csscorechallenge.databinding.FragmentHomeMatchesBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain
import com.example.csscorechallenge.extensions.gone
import com.example.csscorechallenge.extensions.visible
import com.example.csscorechallenge.ui.homematches.adapter.HomeMatchesAdapter
import com.example.csscorechallenge.ui.homematches.viewmodel.HomeMatchesViewModel
import com.example.csscorechallenge.utils.EndlessRecyclerOnScrollListener
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
        Log.d("WTF", "Passou aqui")
        //        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
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
                showOrHideLoading(isShow = true)
            } else {
                showOrHideLoading(isShow = false)
            }
        }

        homeMatchesViewModel.getHomeMatchesLiveData.observe(viewLifecycleOwner) { homeMatches ->
            handleGetHomeMatches(homeMatches)
        }
    }

    private fun handleGetHomeMatches(homeMatchesState: HomeMatchesViewModel.GetHomeMatchesState?) {
        when (homeMatchesState) {
            is HomeMatchesViewModel.GetHomeMatchesState.BindData -> {
                bindData(homeMatchesState.matchList)
            }
            is HomeMatchesViewModel.GetHomeMatchesState.AppendData -> {
                homeMatchesAdapter?.append(homeMatchesState.matchList)
            }
            is HomeMatchesViewModel.GetHomeMatchesState.Failure -> {

            }
            is HomeMatchesViewModel.GetHomeMatchesState.NetworkError -> {

            }
            else -> {}
        }
    }

    private fun bindData(
        matchList: List<HomeMatchesDomain>
    ) {
        val mutableMatchList = matchList.toMutableList()

//        matchList.forEach { match ->
//            if (match.opponents?.size != 2) {
//                if (match.opponents?.size != 1) {
//                    mutableMatchList.remove(match)
//                }
//            }
//        }

        homeMatchesAdapter = HomeMatchesAdapter(mutableMatchList, this)
        binding?.homeMatchesRecyclerView?.apply {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = homeMatchesAdapter
            (binding?.homeMatchesRecyclerView?.itemAnimator as SimpleItemAnimator)
                .supportsChangeAnimations = false

            addOnScrollListener(object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
                override fun onLoadMore(currentPage: Int) {
                    fetchData(currentPage, true)
                }
            })

//            binding?.homeMatchesRecyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    if (!recyclerView.canScrollVertically(1)) {
//                        Log.d("WTF", "Chegou no fim")
//                    }
//                }
//            })
        }
    }

    private fun showOrHideLoading(isShow: Boolean) {
        if (isShow) {
            binding?.homeMatchesProgressBar?.visible()
            binding?.homeMatchesSwipeRefresh?.isRefreshing = false
        } else {
            binding?.homeMatchesProgressBar?.gone()
        }
    }

    private fun fetchData(page: Int = INITIAL_PAGE, appendData: Boolean = false) {
        homeMatchesViewModel.getHomeMatches(page, appendData)
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}