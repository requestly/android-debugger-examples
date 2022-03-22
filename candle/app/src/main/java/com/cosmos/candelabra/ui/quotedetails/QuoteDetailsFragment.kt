package com.cosmos.candelabra.ui.quotedetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.Chart
import com.cosmos.candelabra.data.model.ChartPeriod
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.databinding.FragmentQuoteDetailsBinding
import com.cosmos.candelabra.ui.base.BaseFragment
import com.cosmos.candelabra.ui.common.ChartMarkerView
import com.cosmos.candelabra.util.CurrencyUtil
import com.cosmos.candelabra.util.DateAxisFormatter
import com.cosmos.candelabra.util.extension.formatChange
import com.cosmos.candelabra.util.extension.formatChangePercent
import com.cosmos.candelabra.util.extension.isPortrait
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

@AndroidEntryPoint
class QuoteDetailsFragment : BaseFragment() {

    private var _binding: FragmentQuoteDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: QuoteDetailsFragmentArgs by navArgs()

    private val viewModel: QuoteDetailsViewModel by viewModels()

    private var fetchQuoteJob: Job? = null
    private var fetchChartJob: Job? = null

    private lateinit var quoteDetailsAdapter: QuoteDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_container
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(
                ContextCompat.getColor(requireContext(), R.color.quote_details_accent)
            )
        }

        viewModel.setSymbol(args.symbol)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentView = if (isPortrait()) binding.layoutContent else binding.root
        contentView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { root, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                root.updatePadding(left = insets.left, top = insets.top, right = insets.right)

                windowInsets
            }
        }

        try {
            val behavior = BottomSheetBehavior.from(binding.includeQuoteDetails.root)

            ViewCompat.setOnApplyWindowInsetsListener(
                binding.includeQuoteDetails.root
            ) { details, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                val peekHeight = resources.getDimension(R.dimen.quote_details_peek_height)

                details.updatePadding(bottom = insets.bottom)
                binding.groupPeriod.updatePadding(bottom = insets.bottom)

                behavior.peekHeight = (peekHeight + insets.bottom).toInt()

                windowInsets
            }
        } catch (ignore: IllegalArgumentException) {

        }

        binding.price.setCharacterLists(TickerUtils.provideNumberList())
        binding.change.setCharacterLists(TickerUtils.provideNumberList())
        binding.changePercent.setCharacterLists(TickerUtils.provideNumberList())
        args.quote?.let {
            updateFigures(it)
        }
        initToolbar()
        initChart()
        initPeriodChoices()
        initRecyclerView()
        initDetailsSheet()
        bindViewModel()

        binding.infoRetry.setActionClickListener {
            fetchQuoteInRealTime()
            fetchChartInRealTime()
        }
    }

    private fun initToolbar() {
        binding.symbol.text = args.symbol
        binding.name.text = args.name
        binding.back.setOnClickListener { onBackPressed() }
        binding.toggleQuote.setOnClickListener { viewModel.toggleQuote() }
    }

    private fun initChart() {
        val dataset = LineDataSet(emptyList(), "Prices").apply {
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            cubicIntensity = 0.1F
            lineWidth = 2F
            color = Color.WHITE
            highLightColor = Color.LTGRAY
            setDrawHorizontalHighlightIndicator(false)
            setDrawCircles(false)
            setDrawValues(false)
        }

        val lineData = LineData(dataset)

        val markerView = ChartMarkerView(requireContext(), R.layout.view_chart_marker).apply {
            chartView = binding.chartPrices
        }

        binding.chartPrices.apply {
            setNoDataText("")
            setDrawGridBackground(false)
            setDrawBorders(false)
            setViewPortOffsets(0F, 0F, 0F, 0F)

            axisRight.isEnabled = false
            legend.isEnabled = false
            description.isEnabled = false

            marker = markerView

            data = lineData
        }

        binding.chartPrices.axisLeft.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setDrawLabels(false)
            spaceTop = 25F
            spaceBottom = 25F
        }

        binding.chartPrices.xAxis.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            valueFormatter = DateAxisFormatter(requireContext())
            position = XAxis.XAxisPosition.BOTTOM_INSIDE
            labelCount = 6
            textColor = ContextCompat.getColor(requireContext(), R.color.white_65)
        }
    }

    private fun initPeriodChoices() {
        binding.groupPeriod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_period_day -> viewModel.setPeriod(ChartPeriod.DAY_1)
                R.id.chip_period_week_2 -> viewModel.setPeriod(ChartPeriod.WEEK_2)
                R.id.chip_period_month -> viewModel.setPeriod(ChartPeriod.MONTH_1)
                R.id.chip_period_ytd -> viewModel.setPeriod(ChartPeriod.YTD)
                R.id.chip_period_year -> viewModel.setPeriod(ChartPeriod.YEAR_1)
                R.id.chip_period_max -> viewModel.setPeriod(ChartPeriod.MAX)
            }
        }
    }

    private fun initRecyclerView() {
        quoteDetailsAdapter = QuoteDetailsAdapter()
        binding.includeQuoteDetails.listDetails.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = quoteDetailsAdapter
        }
    }

    private fun initDetailsSheet() {
        if (isPortrait()) {
            BottomSheetBehavior.from(binding.includeQuoteDetails.root).run {
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        // Ignore
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        binding.scrim?.alpha = slideOffset / 2
                    }
                })
            }
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            combine(viewModel.interval, viewModel.chartPeriod) { interval, period ->
                binding.chartPrices.run {
                    fitScreen()
                    highlightValue(null)
                }
                fetchQuoteInRealTime(args.symbol, interval)
                fetchChartInRealTime(args.symbol, period, interval)
            }.collect()
        }
        viewModel.details.asLiveData().observe(viewLifecycleOwner) {
            quoteDetailsAdapter.submitList(it)
        }
        viewModel.isInWatchlist.observe(viewLifecycleOwner) { inWatchlist ->
            val icon = if (inWatchlist) {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_watchlist_remove)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_watchlist_add)
            }
            binding.toggleQuote.setImageDrawable(icon)
        }
    }

    private fun updateChart(chart: Chart, period: ChartPeriod) {
        val dataset = binding.chartPrices.data.getDataSetByIndex(0) as? LineDataSet
        dataset?.apply {
            values = chart.prices
            notifyDataSetChanged()
        }

        (binding.chartPrices.xAxis.valueFormatter as? DateAxisFormatter)?.setPeriod(period)
        (binding.chartPrices.marker as? ChartMarkerView)?.apply {
            currency = chart.currency
            setPeriod(period)
        }

        binding.chartPrices.apply {
            data.notifyDataChanged()
            notifyDataSetChanged()
            invalidate()
            animateX(500)
        }

        binding.noData.isVisible = chart.prices.size <= 1
    }

    private fun updateFigures(quote: Quote) {
        viewModel.setQuote(quote)
        binding.price.text = CurrencyUtil.formatNumber(quote.price, quote.currency)
        binding.change.formatChange(quote.change)
        binding.changePercent.formatChangePercent(quote.changePercent)
    }

    private fun fetchQuoteInRealTime(symbol: String? = null, interval: Long? = null) {
        fetchQuoteJob?.cancel()
        fetchQuoteJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            if (symbol != null && interval != null) {
                viewModel.fetchQuoteInRealTime(symbol, interval).collect {
                    updateQuote(it)
                }
            } else {
                viewModel.fetchQuoteInRealTime().collect {
                    updateQuote(it)
                }
            }
        }
    }

    private fun updateQuote(quote: Resource<Quote>) {
        when (quote) {
            is Resource.Success -> {
                updateFigures(quote.data!!)
            }
            is Resource.Loading -> {
                // TODO
            }
            is Resource.Error -> {
                binding.infoRetry.show()
            }
        }
    }

    private fun fetchChartInRealTime(
        symbol: String? = null,
        chartPeriod: ChartPeriod? = null,
        interval: Long? = null
    ) {
        fetchChartJob?.cancel()
        fetchChartJob = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            if (symbol != null && chartPeriod != null && interval != null) {
                viewModel.fetchChartInRealTime(symbol, chartPeriod, interval).collect {
                    updateChart(it, chartPeriod)
                }
            } else {
                viewModel.fetchChartInRealTime().collect {
                    updateChart(it, viewModel.chartPeriod.value)
                }
            }
        }
    }

    private fun updateChart(chart: Resource<Chart>, chartPeriod: ChartPeriod) {
        when (chart) {
            is Resource.Success -> {
                updateChart(chart.data!!, chartPeriod)
            }
            is Resource.Loading -> {
                // TODO
            }
            is Resource.Error -> {
                binding.infoRetry.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
