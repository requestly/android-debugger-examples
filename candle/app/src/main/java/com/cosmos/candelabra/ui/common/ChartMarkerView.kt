package com.cosmos.candelabra.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.ChartPeriod
import com.cosmos.candelabra.util.CurrencyUtil
import com.cosmos.candelabra.util.DateFormatter
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

@SuppressLint("ViewConstructor")
class ChartMarkerView(
    context: Context,
    @LayoutRes layoutResource: Int = R.layout.view_chart_marker
) : MarkerView(context, layoutResource) {

    private val dateFormatter = DateFormatter(context)

    private val price: TextView = findViewById(R.id.price)
    private val date: TextView = findViewById(R.id.date)

    var currency: String? = null

    fun setPeriod(chartPeriod: ChartPeriod) {
        dateFormatter.period = chartPeriod
    }

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        currency?.let { price.text = CurrencyUtil.formatNumber(e.y.toDouble(), it) }
        date.text = dateFormatter.format(e.x.toLong())
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -(height * 2).toFloat())
    }
}
