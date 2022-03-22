package com.cosmos.candelabra.util

import android.content.Context
import com.cosmos.candelabra.data.model.ChartPeriod
import com.github.mikephil.charting.formatter.ValueFormatter

class DateAxisFormatter(context: Context) : ValueFormatter() {

    private val dateFormatter = DateFormatter(context)

    fun setPeriod(chartPeriod: ChartPeriod) {
        dateFormatter.period = chartPeriod
    }

    override fun getFormattedValue(value: Float): String {
        return dateFormatter.format(value.toLong())
    }
}
