package com.cosmos.candelabra.data.model

enum class Interval(val value: String) {
    MINUTE_1("1m"), MINUTE_2("2m"), MINUTE_5("5m"), MINUTE_15("15m"),
    MINUTE_30("30m"), MINUTE_60("60m"), MINUTE_90("90m"),

    HOUR("1h"),

    DAY_1("1d"), DAY_5("5d"),

    WEEK("1wk"),

    MONTH_1("1mo"), MONTH_3("3mo")
}
