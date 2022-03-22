package com.cosmos.candelabra.util

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Currency

object CurrencyUtil {

    fun formatNumber(price: Double, currencyCode: String): String {
        val currencyFormatter = NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(currencyCode)
            maximumFractionDigits = 2
            roundingMode = RoundingMode.FLOOR
        }
        return currencyFormatter.format(price)
    }
}
