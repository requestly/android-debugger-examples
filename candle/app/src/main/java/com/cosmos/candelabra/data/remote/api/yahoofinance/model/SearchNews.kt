package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchNews(
    @Json(name = "title")
    val title: String,

    @Json(name = "publisher")
    val publisher: String,

    @Json(name = "link")
    val link: String,

    @Json(name = "providerPublishTime")
    val providerPublishTime: Int,

    @Json(name = "type")
    val type: String
)
