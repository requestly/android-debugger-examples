package com.cosmos.candelabra.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cosmos.candelabra.data.local.dao.QuoteDao
import com.cosmos.candelabra.data.model.db.Quote

@Database(
    entities = [Quote::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    companion object {
        suspend fun prepopulateQuoteTable(quoteDao: QuoteDao) {
            quoteDao.insert(
                Quote("AAPL", "Apple Inc.", "USD", 0.0, 0.0, 0.0, 0.0),
                Quote("GOOG", "Alphabet Inc.", "USD", 0.0, 0.0, 0.0, 0.0),
                Quote("TSLA", "Tesla, Inc.", "USD", 0.0, 0.0, 0.0, 0.0),
                Quote("MSFT", "Microsoft Corporation", "USD", 0.0, 0.0, 0.0, 0.0)
            )
        }
    }
}
