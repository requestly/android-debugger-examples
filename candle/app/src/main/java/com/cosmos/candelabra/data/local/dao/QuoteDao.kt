package com.cosmos.candelabra.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.cosmos.candelabra.data.model.db.Quote
import kotlinx.coroutines.flow.Flow

@Dao
abstract class QuoteDao : BaseDao<Quote> {

    @Query("SELECT * FROM quote")
    abstract fun getAllQuotes(): Flow<List<Quote>>

    @Query("DELETE FROM quote WHERE symbol = :symbol")
    abstract suspend fun deleteQuoteFromSymbol(symbol: String)
}
