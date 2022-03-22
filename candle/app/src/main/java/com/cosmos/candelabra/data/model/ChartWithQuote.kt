package com.cosmos.candelabra.data.model

import com.cosmos.candelabra.data.model.db.Quote

data class ChartWithQuote(
    val chart: Chart,

    val quote: Quote
)
