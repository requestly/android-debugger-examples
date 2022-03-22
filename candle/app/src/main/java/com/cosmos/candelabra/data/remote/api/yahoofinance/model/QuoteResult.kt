package com.cosmos.candelabra.data.remote.api.yahoofinance.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuoteResult(
    @Json(name = "language")
    val language: String,

    @Json(name = "region")
    val region: String,

    @Json(name = "quoteType")
    val quoteType: String,

    @Json(name = "currency")
    val currency: String,

    @Json(name = "exchange")
    val exchange: String,

    @Json(name = "shortName")
    val shortName: String,

    @Json(name = "longName")
    val longName: String,

    @Json(name = "gmtOffSetMilliseconds")
    val gmtOffSetMilliseconds: Long,

    @Json(name = "regularMarketChange")
    val regularMarketChange: Double,

    @Json(name = "regularMarketChangePercent")
    val regularMarketChangePercent: Double,

    @Json(name = "regularMarketPrice")
    val regularMarketPrice: Double,

    @Json(name = "regularMarketDayHigh")
    val regularMarketDayHigh: Double,

    @Json(name = "regularMarketDayLow")
    val regularMarketDayLow: Double,

    @Json(name = "regularMarketPreviousClose")
    val regularMarketPreviousClose: Double,

    @Json(name = "regularMarketOpen")
    val regularMarketOpen: Double,

    @Json(name = "regularMarketVolume")
    val regularMarketVolume: Long,

    @Json(name = "trailingPE")
    val trailingPE: Double?,

    @Json(name = "marketState")
    val marketState: String,

    @Json(name = "tradeable")
    val tradeable: Boolean,

    @Json(name = "fiftyTwoWeekLowChange")
    val fiftyTwoWeekLowChange: Double?,

    @Json(name = "fiftyTwoWeekLowChangePercent")
    val fiftyTwoWeekLowChangePercent: Double?,

    @Json(name = "fiftyTwoWeekHighChange")
    val fiftyTwoWeekHighChange: Double?,

    @Json(name = "fiftyTwoWeekHighChangePercent")
    val fiftyTwoWeekHighChangePercent: Double?,

    @Json(name = "fiftyTwoWeekLow")
    val fiftyTwoWeekLow: Double?,

    @Json(name = "fiftyTwoWeekHigh")
    val fiftyTwoWeekHigh: Double?,

    @Json(name = "dividendDate")
    val dividendDate: Long?,

    @Json(name = "earningsTimestamp")
    val earningsTimestamp: Long?,

    @Json(name = "trailingAnnualDividendRate")
    val trailingAnnualDividendRate: Double?,

    @Json(name = "fiftyDayAverage")
    val fiftyDayAverage: Double?,

    @Json(name = "fiftyDayAverageChange")
    val fiftyDayAverageChange: Double?,

    @Json(name = "fiftyDayAverageChangePercent")
    val fiftyDayAverageChangePercent: Double?,

    @Json(name = "twoHundredDayAverage")
    val twoHundredDayAverage: Double?,

    @Json(name = "twoHundredDayAverageChange")
    val twoHundredDayAverageChange: Double?,

    @Json(name = "twoHundredDayAverageChangePercent")
    val twoHundredDayAverageChangePercent: Double?,

    @Json(name = "marketCap")
    val marketCap: Long?,

    @Json(name = "symbol")
    val symbol: String
)
