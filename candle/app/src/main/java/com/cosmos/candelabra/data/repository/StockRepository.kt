package com.cosmos.candelabra.data.repository

import com.cosmos.candelabra.data.local.StockDatabase
import com.cosmos.candelabra.data.model.db.Quote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val stockDatabase: StockDatabase
) : BaseRepository() {

    fun getAllQuotes(): Flow<List<Quote>> = stockDatabase.quoteDao().getAllQuotes()

    suspend fun insertQuote(quote: Quote) {
        stockDatabase.quoteDao().insert(quote)
    }

    suspend fun deleteQuote(symbol: String) {
        stockDatabase.quoteDao().deleteQuoteFromSymbol(symbol)
    }
}
