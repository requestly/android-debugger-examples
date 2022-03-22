package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chart(
    @Json(name = "result")
    val result: List<ChartResult>

    // TODO
    // @Json(name = "error")
    // val error: Any? = null
)
