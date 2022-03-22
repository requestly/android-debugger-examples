package com.cosmos.candelabra.data.local.mapper

import com.cosmos.candelabra.data.model.QuoteType
import com.cosmos.candelabra.data.model.SearchItem
import com.cosmos.candelabra.data.remote.api.autoc.model.Result
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Search
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.SearchQuote
import java.util.Locale

typealias SearchAutoc = com.cosmos.candelabra.data.remote.api.autoc.model.Search

object SearchMapper {

    fun dataToEntities(data: SearchAutoc): List<SearchItem> {
        return data.resultSet.result.map { dataToEntity(it) }
    }

    fun dataToEntity(data: Result): SearchItem.Quote {
        return with(data) {
            SearchItem.Quote(
                symbol,
                name,
                exchDisp,
                QuoteType.fromString(typeDisp.toUpperCase(Locale.ROOT))
            )
        }
    }

    fun dataToEntities(data: Search): List<SearchItem> {
        return data.quotes.map { dataToEntity(it) }
    }

    fun dataToEntity(data: SearchQuote): SearchItem.Quote {
        return with(data) {
            SearchItem.Quote(
                symbol,
                shortname ?: longname ?: "",
                exchange,
                QuoteType.fromString(quoteType)
            )
        }
    }
}
