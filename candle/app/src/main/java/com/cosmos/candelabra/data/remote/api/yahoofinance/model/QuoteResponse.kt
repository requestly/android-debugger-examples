package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuoteResponse(
    @Json(name = "result")
    val result: List<QuoteResult>

    // TODO
    // @Json(name = "error")
    // val error: Any? = null
)
