package com.cosmos.candelabra.ui.watchlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.BlurTransformation
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.ChartWithQuote
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.databinding.ItemQuoteWatchlistBinding
import com.cosmos.candelabra.util.CurrencyUtil
import com.cosmos.candelabra.util.extension.formatChangePercent
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.robinhood.ticker.TickerUtils

class WatchlistAdapter(
    private val onClick: (View, Quote) -> Unit
) : ListAdapter<ChartWithQuote, WatchlistAdapter.ViewHolder>(CHART_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemQuoteWatchlistBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.update(getItem(position))
        }
    }

    fun updateList(charts: List<ChartWithQuote>) {
        val newList = mutableListOf(*currentList.toTypedArray())

        charts.forEach { newChart ->
            val oldChartIndex = newList.indexOfFirst { it.chart.symbol == newChart.chart.symbol }
            if (oldChartIndex != -1) {
                newList[oldChartIndex] = newChart
            }
        }

        submitList(newList)
    }

    inner class ViewHolder(
        private val binding: ItemQuoteWatchlistBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.price.setCharacterLists(TickerUtils.provideNumberList())
            binding.changePercent.setCharacterLists(TickerUtils.provideNumberList())
        }

        fun bind(chart: ChartWithQuote) {
            binding.chart = chart

            binding.root.setOnClickListener { onClick.invoke(binding.layoutRoot, chart.quote) }

            binding.background.load(R.drawable.quote_card_background) {
                scale(Scale.FILL)
                transformations(BlurTransformation(binding.background.context, 25F, 4F))
            }

            val dataSet = LineDataSet(chart.chart.prices, "Prices").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.2F
                isHighlightEnabled = false
                lineWidth = 1F
                color = Color.WHITE
                setDrawCircles(false)
                setDrawValues(false)
            }

            val lineData = LineData(dataSet)

            binding.chartPrices.apply {
                setNoDataText("")
                setDrawGridBackground(false)
                setDrawBorders(false)
                setPinchZoom(false)
                setTouchEnabled(false)
                setViewPortOffsets(0F, 10F, 0F, 10F)

                xAxis.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = false
                description.isEnabled = false

                data = lineData
            }

            binding.chartPrices.axisLeft.apply {
                setDrawGridLines(false)
                setDrawAxisLine(false)
                setDrawLabels(false)
                spaceTop = 25F
                spaceBottom = 25F
            }

            updateFigures(chart)
        }

        fun update(chart: ChartWithQuote) {
            updateFigures(chart)

            val previousClose = chart.chart.previousClose.toFloat()
            val previousCloseLine = LimitLine(previousClose).apply {
                lineWidth = 0.2F
                lineColor = Color.LTGRAY
                enableDashedLine(10F, 10F, 0F)
            }

            binding.chartPrices.axisLeft.apply {
                removeAllLimitLines()
                addLimitLine(previousCloseLine)
            }

            val dataset = binding.chartPrices.data.getDataSetByIndex(0) as? LineDataSet
            dataset?.apply {
                values = chart.chart.prices
                notifyDataSetChanged()
            }

            binding.chartPrices.apply {
                data.notifyDataChanged()
                notifyDataSetChanged()
                invalidate()
                animateX(500)
            }

            binding.noData.isVisible = chart.chart.prices.size <= 1
        }

        private fun updateFigures(chart: ChartWithQuote) {
            binding.price.text = CurrencyUtil.formatNumber(chart.chart.price, chart.quote.currency)
            binding.previousClose.text = CurrencyUtil.formatNumber(
                chart.chart.previousClose,
                chart.quote.currency
            )
            binding.changePercent.formatChangePercent(chart.quote.changePercent)
        }
    }

    companion object {
        private val CHART_COMPARATOR = object : DiffUtil.ItemCallback<ChartWithQuote>() {
            override fun areItemsTheSame(
                oldItem: ChartWithQuote,
                newItem: ChartWithQuote
            ): Boolean {
                return oldItem.chart.symbol == newItem.chart.symbol
            }

            override fun areContentsTheSame(
                oldItem: ChartWithQuote,
                newItem: ChartWithQuote
            ): Boolean {
                return oldItem.chart.price == newItem.chart.price &&
                        oldItem.chart.prices.size == newItem.chart.prices.size
            }

            override fun getChangePayload(oldItem: ChartWithQuote, newItem: ChartWithQuote): Any {
                return newItem
            }
        }
    }
}
