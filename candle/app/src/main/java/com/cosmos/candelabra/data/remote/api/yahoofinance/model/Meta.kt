package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @Json(name = "currency")
    val currency: String,

    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "exchangeName")
    val exchangeName: String,

    @Json(name = "instrumentType")
    val instrumentType: String,

    @Json(name = "regularMarketPrice")
    val regularMarketPrice: Double,

    @Json(name = "chartPreviousClose")
    val previousClose: Double,

    @Json(name = "currentTradingPeriod")
    val currentTradingPeriod: TradingPeriod,
)
