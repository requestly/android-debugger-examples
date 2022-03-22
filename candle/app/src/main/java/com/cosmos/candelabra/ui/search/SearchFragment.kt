package com.cosmos.candelabra.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.transition.Slide
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.SearchItem
import com.cosmos.candelabra.databinding.FragmentSearchBinding
import com.cosmos.candelabra.ui.base.BaseFragment
import com.cosmos.candelabra.ui.quotedetails.QuoteDetailsFragmentDirections
import com.cosmos.candelabra.util.extension.themeColor
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        initAnimations()
        initToolbar()
        initRecyclerView()
        bindViewModel()

        binding.infoRetry.setActionClickListener { viewModel.forceRefresh() }
    }

    private fun initAnimations() {
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab_add)
            endView = binding.root
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            containerColor = ContextCompat.getColor(requireContext(), R.color.background)
            startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
            endContainerColor = ContextCompat.getColor(requireContext(), R.color.background)
        }
        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
            addTarget(binding.root)
        }
    }

    private fun initToolbar() {
        binding.searchInput.addTextChangedListener(
            afterTextChanged = {
                viewModel.setQuery(it.toString())
            }
        )
        binding.back.setOnClickListener { onBackPressed() }
        binding.clear.setOnClickListener { binding.searchInput.text?.clear() }
    }

    private fun initRecyclerView() {
        searchAdapter = SearchAdapter { cardView, quote ->
            openDetails(cardView, quote)
        }
        binding.listSearch.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = searchAdapter
        }
    }

    private fun bindViewModel() {
        viewModel.items.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    searchAdapter.submitList(it.data!!)
                }
                is Resource.Loading -> {
                    // TODO
                }
                is Resource.Error -> {
                    binding.infoRetry.show()
                }
            }
        }
    }

    private fun openDetails(cardView: View, quote: SearchItem.Quote) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        val transitionName = getString(R.string.quote_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to transitionName)
        val directions = QuoteDetailsFragmentDirections.openQuoteDetails(quote.symbol, quote.name)

        findNavController().navigate(directions, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
