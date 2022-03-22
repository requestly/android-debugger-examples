package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Period(
    @Json(name = "timezone")
    val timezone: String,

    @Json(name = "start")
    val start: Long,

    @Json(name = "end")
    val end: Long,

    @Json(name = "gmtoffset")
    val gmtoffset: Int
) {
    val inPeriod: Boolean
        get() = System.currentTimeMillis() in start.times(1000)..end.times(1000)
}
