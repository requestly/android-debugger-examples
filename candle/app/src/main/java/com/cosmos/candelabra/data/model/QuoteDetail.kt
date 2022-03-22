package com.cosmos.candelabra.data.model

import androidx.annotation.StringRes

data class QuoteDetail(
    @StringRes
    val title: Int,

    val data: String
)
