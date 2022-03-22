package com.cosmos.candelabra.data.model.db

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "quote")
data class Quote @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "symbol", collate = ColumnInfo.NOCASE)
    val symbol: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "currency")
    val currency: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "previousClose")
    val previousClose: Double,

    @ColumnInfo(name = "change")
    val change: Double,

    @ColumnInfo(name = "changePercent")
    val changePercent: Double,

    @Ignore
    val open: Double? = null,

    @Ignore
    val dayLow: Double? = null,

    @Ignore
    val dayHigh: Double? = null,

    @Ignore
    val ftwLow: Double? = null,

    @Ignore
    val ftwHigh: Double? = null,

    @Ignore
    val volume: Long? = null,

    @Ignore
    val markerCap: Long? = null,

    @Ignore
    val peRatio: Double? = null,

    @Ignore
    val earningsDate: Long? = null,

    @Ignore
    val dividendRate: Double? = null,

    @Ignore
    val dividendDate: Long? = null
) : Parcelable
