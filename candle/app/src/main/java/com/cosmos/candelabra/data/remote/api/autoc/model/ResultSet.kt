package com.cosmos.candelabra.data.remote.api.autoc.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResultSet(
    @Json(name = "Query")
    val query: String,
    @Json(name = "Result")
    val result: List<Result>
)
