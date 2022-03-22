package com.cosmos.candelabra.data.model

import androidx.annotation.StringRes
import com.cosmos.candelabra.R

enum class QuoteType(val value: String, @StringRes val text: Int) {
    EQUITY("EQUITY", R.string.quote_type_equity),
    ETF("ETF", R.string.quote_type_etf),
    CURRENCY("CURRENCY", R.string.quote_type_currency);

    companion object {
        fun fromString(type: String): QuoteType {
            return values().find { it.value == type } ?: EQUITY
        }
    }
}
