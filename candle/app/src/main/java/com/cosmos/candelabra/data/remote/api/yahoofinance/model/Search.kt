package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "count")
    val count: Int,

    @Json(name = "quotes")
    val quotes: List<SearchQuote>,

    @Json(name = "news")
    val news: List<SearchNews>
)
