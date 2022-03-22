package com.cosmos.candelabra.data.local.mapper

import com.cosmos.candelabra.data.model.Chart
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.ChartResult
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Charts
import com.github.mikephil.charting.data.Entry

object ChartMapper {

    fun dataToEntity(data: ChartResult): Chart {
        return with(data) {
            Chart(
                meta.symbol,
                meta.currency,
                meta.regularMarketPrice,
                meta.previousClose,
                mapDatesWithPrices(timestamp, indicators.quote.firstOrNull()?.close ?: emptyList())
            )
        }
    }

    fun dataToEntity(data: Charts): Chart? {
        data.chart.result.firstOrNull()?.let {
            return dataToEntity(it)
        }
        return null
    }

    private fun mapDatesWithPrices(
        timestamps: List<Long>,
        prices: List<Double?>
    ): List<Entry> {
        return timestamps.zip(prices).mapNotNull { pair ->
            pair.second?.let { price ->
                Entry(
                    pair.first.times(1000).toFloat(), // Convert timestamps to milliseconds
                    price.toFloat()
                )
            }
        }
    }
}
