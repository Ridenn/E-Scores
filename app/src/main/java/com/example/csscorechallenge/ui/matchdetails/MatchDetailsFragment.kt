package com.example.csscorechallenge.ui.matchdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.csscorechallenge.R
import com.example.csscorechallenge.databinding.FragmentMatchDetailsBinding
import com.example.csscorechallenge.domain.model.HomeMatchesDomain


class MatchDetailsFragment : Fragment() {

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

//        (activity as AppCompatActivity).supportActionBar

        val match: HomeMatchesDomain =
            MatchDetailsFragmentArgs.fromBundle(requireArguments()).selectedMatch

        bindData(match)

    }

    private fun bindData(match: HomeMatchesDomain) {

    }
}