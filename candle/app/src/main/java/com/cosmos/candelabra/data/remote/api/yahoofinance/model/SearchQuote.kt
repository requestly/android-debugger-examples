package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchQuote(
    @Json(name = "exchange")
    val exchange: String,

    @Json(name = "shortname")
    val shortname: String?,

    @Json(name = "quoteType")
    val quoteType: String,

    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "longname")
    val longname: String?,

    @Json(name = "isYahooFinance")
    val isYahooFinance: Boolean
)
