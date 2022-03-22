package com.cosmos.candelabra.data.model

import com.github.mikephil.charting.data.Entry

data class Chart(
    val symbol: String,

    val currency: String,

    val price: Double,

    val previousClose: Double,

    val prices: List<Entry>
)
