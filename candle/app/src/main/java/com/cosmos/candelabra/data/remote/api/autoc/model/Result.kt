package com.cosmos.candelabra.data.remote.api.autoc.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "symbol")
    val symbol: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "exch")
    val exch: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "exchDisp")
    val exchDisp: String,
    @Json(name = "typeDisp")
    val typeDisp: String
)
