package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TradingPeriod(
    @Json(name = "pre")
    val pre: Period,

    @Json(name = "regular")
    val regular: Period,

    @Json(name = "post")
    val post: Period
)
