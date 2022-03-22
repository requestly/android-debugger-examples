package com.cosmos.candelabra.data.remote.api.autoc

import com.cosmos.candelabra.data.remote.api.autoc.model.Search
import retrofit2.http.GET
import retrofit2.http.Query

interface AutocApi {

    @GET("/autoc?region=US&lang=en-US")
    suspend fun search(@Query("query") query: String): Search

    companion object {
        const val BASE_URL = "https://autoc.finance.yahoo.com/"
    }
}
