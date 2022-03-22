package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChartResult(
    @Json(name = "meta")
    val meta: Meta,

    @Json(name = "timestamp")
    val timestamp: List<Long> = listOf(),

    @Json(name = "indicators")
    val indicators: Indicators
)
