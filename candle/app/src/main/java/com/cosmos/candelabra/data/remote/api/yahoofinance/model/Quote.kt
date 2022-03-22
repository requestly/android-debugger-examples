package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Quote(
    @Json(name = "close")
    val close: List<Double?> = listOf(),

    @Json(name = "low")
    val low: List<Double?> = listOf(),

    @Json(name = "volume")
    val volume: List<Long?> = listOf(),

    @Json(name = "open")
    val `open`: List<Double?> = listOf(),

    @Json(name = "high")
    val high: List<Double?> = listOf()
)
