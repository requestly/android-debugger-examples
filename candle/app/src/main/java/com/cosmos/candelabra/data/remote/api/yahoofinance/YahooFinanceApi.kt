package com.cosmos.candelabra.data.remote.api.yahoofinance

import com.cosmos.candelabra.data.model.Interval
import com.cosmos.candelabra.data.model.Range
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Charts
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Quotes
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Search
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YahooFinanceApi {

    @GET("/v7/finance/quote")
    suspend fun getQuotes(@Query("symbols") symbols: String): Quotes

    @GET("/v8/finance/chart/{symbols}")
    suspend fun getChart(
        @Path("symbols") symbols: String,
        @Query("range") range: Range,
        @Query("interval") interval: Interval
    ): Charts

    @GET("/v1/finance/search")
    suspend fun search(@Query("q") query: String): Search

    companion object {
        const val BASE_URL = "https://query1.finance.yahoo.com/"
    }
}
