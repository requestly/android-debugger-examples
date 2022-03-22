package com.cosmos.candelabra.data.model

enum class ChartPeriod(val range: Range, val interval: Interval) {
    DAY_1(Range.DAY_1, Interval.MINUTE_15),

    WEEK_2(Range.WEEK_2, Interval.DAY_1),

    MONTH_1(Range.MONTH_1, Interval.DAY_1), MONTH_6(Range.MONTH_6, Interval.DAY_1),

    YEAR_1(Range.YEAR_1, Interval.WEEK), YEAR_5(Range.YEAR_5, Interval.MONTH_1),

    YTD(Range.YTD, Interval.DAY_1), MAX(Range.MAX, Interval.MONTH_1)
}
