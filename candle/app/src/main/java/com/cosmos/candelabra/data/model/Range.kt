package com.cosmos.candelabra.data.model

enum class Range(val value: String) {
    DAY_1("1d"), DAY_5("5d"),

    WEEK_2("2wk"),

    MONTH_1("1mo"), MONTH_3("3mo"), MONTH_6("6mo"),

    YEAR_1("1y"), YEAR_2("2y"), YEAR_5("5y"), YEAR_10("10y"),

    YTD("ytd"), MAX("max")
}
