package com.ricardaparicio.cryptodemo.features.common.data.api

import com.ricardaparicio.cryptodemo.features.common.data.api.model.CoinApiModel
import com.ricardaparicio.cryptodemo.features.common.data.api.model.CoinSummaryApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApiService {
    @GET("coins/markets")
    fun getCoins(
        @Query("vs_currency") currency: String,
        @Query("per_page") itemsPerPage: Int = 20,
        @Query("sparkline") sparkLine: Boolean = false,
    ): Call<List<CoinSummaryApiModel>>

    @GET("coins/{id}")
    fun getCoin(@Path("id") coinId: String): Call<CoinApiModel>
}