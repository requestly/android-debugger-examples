package com.cosmos.candelabra.data.model

sealed class SearchItem {

    data class Quote(
        val symbol: String,
        val name: String,
        val exchange: String,
        val quoteType: QuoteType
    ) : SearchItem()
}
