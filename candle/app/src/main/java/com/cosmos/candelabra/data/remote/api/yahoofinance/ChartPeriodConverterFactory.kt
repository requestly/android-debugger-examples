package com.cosmos.candelabra.data.remote.api.yahoofinance

import com.cosmos.candelabra.data.model.Interval
import com.cosmos.candelabra.data.model.Range
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ChartPeriodConverterFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        return when (type) {
            Range::class.java -> {
                Converter<Range, String> { it.value }
            }
            Interval::class.java -> {
                Converter<Interval, String> { it.value }
            }
            else -> super.stringConverter(type, annotations, retrofit)
        }
    }
}
