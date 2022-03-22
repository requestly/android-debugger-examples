package com.cosmos.candelabra.data.remote.api.autoc.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "ResultSet")
    val resultSet: ResultSet
)
