package com.cosmos.candelabra.data.local.mapper

import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.QuoteResult

object QuoteMapper {

    fun dataToEntity(data: QuoteResult): Quote {
        return with(data) {
            Quote(
                symbol,
                shortName,
                currency,
                regularMarketPrice,
                regularMarketPreviousClose,
                regularMarketChange,
                regularMarketChangePercent,
                regularMarketOpen,
                regularMarketDayLow,
                regularMarketDayHigh,
                fiftyTwoWeekLow,
                fiftyTwoWeekHigh,
                regularMarketVolume,
                marketCap,
                trailingPE,
                earningsTimestamp?.times(1000),
                trailingAnnualDividendRate,
                dividendDate?.times(1000)
            )
        }
    }

    fun dataToEntities(data: List<QuoteResult>): List<Quote> {
        return data.map {
            dataToEntity(it)
        }
    }
}
