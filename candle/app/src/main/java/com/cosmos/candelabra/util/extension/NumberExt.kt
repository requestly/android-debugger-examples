package com.cosmos.candelabra.util.extension

import android.content.Context
import com.cosmos.candelabra.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.format(fractionDigits: Int = 2): String {
    return NumberFormat.getInstance(Locale.getDefault()).run {
        minimumFractionDigits = fractionDigits
        maximumFractionDigits = fractionDigits
        format(this@format)
    }
}

fun Long.format(): String {
    return NumberFormat.getInstance(Locale.getDefault()).format(this)
}

fun Long.formatDate(format: String): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(Date(this))
}

fun Long.formatBigNumbers(context: Context): String {
    return when {
        this < 100_000 -> NumberFormat.getInstance(Locale.getDefault()).format(this)
        this < 1_000_000 -> context.getString(R.string.number_format_thousands, this.div(1000.0))
        this < 1_000_000_000 -> {
            context.getString(R.string.number_format_millions, this.div(1000000.0))
        }
        this < 1_000_000_000_000 -> {
            context.getString(R.string.number_format_billions, this.div(1000000000.0))
        }
        else -> {
            context.getString(R.string.number_format_trillions, this.div(1000000000000.0))
        }
    }
}
