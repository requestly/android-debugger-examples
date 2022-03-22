package com.cosmos.candelabra.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.ChartWithQuote
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.databinding.FragmentWatchlistBinding
import com.cosmos.candelabra.ui.base.BaseFragment
import com.cosmos.candelabra.ui.quotedetails.QuoteDetailsFragmentDirections
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WatchlistFragment : BaseFragment() {

    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by viewModels()

    private var fetchJob: Job? = null

    private lateinit var watchlistAdapter: WatchlistAdapter

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
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        initToolbar()
        initRecyclerView()
        bindViewModel()

        binding.infoRetry.setActionClickListener { fetchChartsInRealTime() }
    }

    private fun initToolbar() {
        binding.includeAppbar.toolbar.title = getString(R.string.watchlist_fragment_title)
    }

    private fun initRecyclerView() {
        watchlistAdapter = WatchlistAdapter { cardView, quote ->
            openDetails(cardView, quote)
        }
        binding.listQuotes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = watchlistAdapter
        }
    }

    private fun bindViewModel() {
        viewModel.userCharts.observe(viewLifecycleOwner) {
            watchlistAdapter.submitList(it) {
                fetchChartsInRealTime(it)
            }
        }
    }

    private fun fetchChartsInRealTime(charts: List<ChartWithQuote>? = null) {
        fetchJob?.cancel()
        fetchJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            charts?.let { _charts ->
                viewModel.fetchChartsInRealTime(_charts).collect {
                    updateCharts(it)
                }
            } ?: run {
                viewModel.fetchChartsInRealTime().collect {
                    updateCharts(it)
                }
            }
        }
    }

    private fun updateCharts(charts: Resource<List<ChartWithQuote>>) {
        when (charts) {
            is Resource.Success -> {
                watchlistAdapter.updateList(charts.data!!)
            }
            is Resource.Loading -> {
                // TODO
            }
            is Resource.Error -> {
                binding.infoRetry.show()
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
