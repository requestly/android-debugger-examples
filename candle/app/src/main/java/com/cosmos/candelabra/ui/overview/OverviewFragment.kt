package com.cosmos.candelabra.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.databinding.FragmentOverviewBinding
import com.cosmos.candelabra.ui.base.BaseFragment
import com.cosmos.candelabra.ui.quotedetails.QuoteDetailsFragmentDirections
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OverviewFragment : BaseFragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by viewModels()

    private var fetchJob: Job? = null

    private lateinit var overviewAdapter: OverviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        initToolbar()
        initRecyclerView()
        bindViewModel()

        binding.infoRetry.setActionClickListener { fetchQuotesInRealTime() }
    }

    private fun initToolbar() {
        binding.includeAppbar.toolbar.title = getString(R.string.overview_fragment_title)
    }

    private fun initRecyclerView() {
        overviewAdapter = OverviewAdapter { cardView, quote ->
            openDetails(cardView, quote)
        }
        binding.listQuotes.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = overviewAdapter
        }
    }

    private fun bindViewModel() {
        viewModel.userQuotes.observe(viewLifecycleOwner) {
            overviewAdapter.submitList(it) {
                fetchQuotesInRealTime(it)
            }
        }
    }

    private fun fetchQuotesInRealTime(quotes: List<Quote>? = null) {
        fetchJob?.cancel()
        fetchJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            quotes?.let { _quotes ->
                viewModel.fetchQuotesInRealTime(_quotes).collect {
                    updateQuotes(it)
                }
            } ?: run {
                viewModel.fetchQuotesInRealTime().collect {
                    updateQuotes(it)
                }
            }
        }
    }

    private fun updateQuotes(quotes: Resource<List<Quote>>) {
        when (quotes) {
            is Resource.Error -> {
                binding.infoRetry.show()
            }
            is Resource.Loading -> {
                // TODO
            }
            is Resource.Success -> {
                overviewAdapter.updateList(quotes.data!!)
            }
        }
    }

    private fun openDetails(cardView: View, quote: Quote) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        val transitionName = getString(R.string.quote_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to transitionName)
        val directions = QuoteDetailsFragmentDirections.openQuoteDetails(
            quote.symbol,
            quote.name,
            quote
        )

        findNavController().navigate(directions, extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
