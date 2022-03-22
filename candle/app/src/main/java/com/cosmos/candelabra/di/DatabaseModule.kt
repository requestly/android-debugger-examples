package com.cosmos.candelabra.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cosmos.candelabra.data.local.StockDatabase
import com.cosmos.candelabra.data.local.dao.QuoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    private lateinit var stockDatabase: StockDatabase

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        stockDatabase = Room.databaseBuilder(context, StockDatabase::class.java, "stock_db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        StockDatabase.prepopulateQuoteTable(stockDatabase.quoteDao())
                    }
                }
            })
            .build()
        return stockDatabase
    }

    @Provides
    @Singleton
    fun provideQuoteDao(stockDatabase: StockDatabase): QuoteDao = stockDatabase.quoteDao()
}
