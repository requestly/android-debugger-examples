package com.cosmos.candelabra.util.extension

import androidx.core.content.ContextCompat
import com.cosmos.candelabra.R
import com.robinhood.ticker.TickerView

fun TickerView.formatChange(change: Double) {
    when {
        change > 0 -> {
            text = context.getString(R.string.quote_change_pos, change)
            textColor = ContextCompat.getColor(context, R.color.change_positive)
        }
        change < 0 -> {
            text = context.getString(R.string.quote_change_neg, change)
            textColor = ContextCompat.getColor(context, R.color.change_negative)
        }
        else -> {
            text = context.getString(R.string.quote_change_neg, change)
            textColor = ContextCompat.getColor(context, R.color.white)
        }
    }
}

fun TickerView.formatChangePercent(changePercent: Double) {
    when {
        changePercent > 0 -> {
            text = context.getString(R.string.quote_change_percent_pos, changePercent)
            textColor = ContextCompat.getColor(context, R.color.change_positive)
        }
        changePercent < 0 -> {
            text = context.getString(R.string.quote_change_percent_neg, changePercent)
            textColor = ContextCompat.getColor(context, R.color.change_negative)
        }
        else -> {
            text = context.getString(R.string.quote_change_percent_neg, changePercent)
            textColor = ContextCompat.getColor(context, R.color.white)
        }
    }
}
