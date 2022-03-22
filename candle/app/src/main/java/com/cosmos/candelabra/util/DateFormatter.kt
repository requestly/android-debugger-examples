package com.cosmos.candelabra.util

import android.content.Context
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.ChartPeriod
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateFormatter(context: Context) {

    private val hoursPattern by lazy { context.getString(R.string.chart_label_hours) }
    private val daysPattern by lazy { context.getString(R.string.chart_label_days) }
    private val yearsPattern by lazy { context.getString(R.string.chart_label_years) }

    private var format = SimpleDateFormat(hoursPattern, Locale.getDefault())

    var period: ChartPeriod = ChartPeriod.DAY_1
        set(value) {
            when (value) {
                ChartPeriod.DAY_1 -> format.applyPattern(hoursPattern)
                ChartPeriod.WEEK_2,
                ChartPeriod.MONTH_1,
                ChartPeriod.MONTH_6,
                ChartPeriod.YTD,
                ChartPeriod.YEAR_1 -> format.applyPattern(daysPattern)
                ChartPeriod.YEAR_5,
                ChartPeriod.MAX -> format.applyPattern(yearsPattern)
            }
            field = value
        }

    fun format(timeInMillis: Long): String {
        return format.format(Date(timeInMillis))
    }
}
