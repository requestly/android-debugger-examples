package com.cosmos.candelabra.ui.quotedetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cosmos.candelabra.R
import com.cosmos.candelabra.data.model.Chart
import com.cosmos.candelabra.data.model.ChartPeriod
import com.cosmos.candelabra.data.model.QuoteDetail
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.data.repository.StockRepository
import com.cosmos.candelabra.data.repository.YahooFinanceRepository
import com.cosmos.candelabra.util.CurrencyUtil
import com.cosmos.candelabra.util.extension.format
import com.cosmos.candelabra.util.extension.formatBigNumbers
import com.cosmos.candelabra.util.extension.formatDate
import com.cosmos.candelabra.util.extension.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class QuoteDetailsViewModel @Inject constructor(
    @ApplicationContext applicationContext: Context,
    private val stockRepository: StockRepository,
    private val yahooFinanceRepository: YahooFinanceRepository
) : ViewModel() {

    private val _symbol: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _interval: MutableStateFlow<Long> = MutableStateFlow(DEFAULT_INTERVAL)
    val interval: StateFlow<Long> get() = _interval

    private val _chartPeriod: MutableStateFlow<ChartPeriod> = MutableStateFlow(DEFAULT_PERIOD)
    val chartPeriod: StateFlow<ChartPeriod> get() = _chartPeriod

    private val _quote: MutableStateFlow<Quote?> = MutableStateFlow(null)
    val quote: StateFlow<Quote?> get() = _quote

    val isInWatchlist: LiveData<Boolean> = _symbol.transform { _symbol ->
        _symbol?.let { symbol ->
            stockRepository.getAllQuotes().map { quotes ->
                emit(quotes.any { it.symbol.equals(symbol, true) })
            }.collect()
        }
    }.asLiveData()

    val details: Flow<List<QuoteDetail>> = _quote.transform { quote ->
        quote?.run {
            val details = mutableListOf<QuoteDetail>()

            open?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_open,
                        CurrencyUtil.formatNumber(it, currency)
                    )
                )
            }

            if (dayLow != null && dayHigh != null) {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_day_range,
                        "${dayLow.format()} - ${dayHigh.format()}"
                    )
                )
            }

            if (ftwLow != null && ftwHigh != null) {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_ftw_range,
                        "${ftwLow.format()} - ${ftwHigh.format()}"
                    )
                )
            }

            volume?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_volume,
                        it.format()
                    )
                )
            }

            markerCap?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_market_cap,
                        it.formatBigNumbers(applicationContext)
                    )
                )
            }

            peRatio?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_pe_ratio,
                        it.format()
                    )
                )
            }

            earningsDate?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_earnings_date,
                        it.formatDate(applicationContext.getString(R.string.date_format_long))
                    )
                )
            }

            dividendRate?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_dividend_rate,
                        it.format()
                    )
                )
            }

            dividendDate?.let {
                details.add(
                    QuoteDetail(
                        R.string.quote_details_dividend_date,
                        it.formatDate(applicationContext.getString(R.string.date_format_long))
                    )
                )
            }

            emit(details)
        }
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    fun fetchQuoteInRealTime(
        symbol: String? = _symbol.value,
        interval: Long? = _interval.value,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<Quote>> {
        if (symbol != null && interval != null) {
            return yahooFinanceRepository.getQuoteInRealTime(
                symbol,
                interval,
                coroutineContext = coroutineContext
            )
        }
        return flow { emit(Resource.Error()) }
    }

    fun fetchChartInRealTime(
        symbol: String? = _symbol.value,
        chartPeriod: ChartPeriod = _chartPeriod.value,
        interval: Long = _interval.value,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<Chart>> {
        if (symbol != null) {
            return yahooFinanceRepository.getChartInRealTime(
                symbol,
                chartPeriod,
                interval,
                coroutineContext
            )
        }
        return flow { emit(Resource.Error()) }
    }

    fun setSymbol(symbol: String) {
        _symbol.updateValue(symbol)
    }

    fun setInterval(interval: Long) {
        _interval.updateValue(interval)
    }

    fun setPeriod(period: ChartPeriod) {
        _chartPeriod.updateValue(period)
    }

    fun setQuote(quote: Quote) {
        _quote.updateValue(quote)
    }

    fun toggleQuote() {
        viewModelScope.launch {
            if (isInWatchlist.value == true) {
                _symbol.value?.let { stockRepository.deleteQuote(it) }
            } else {
                _quote.value?.let { stockRepository.insertQuote(it) }
            }
        }
    }

    companion object {
        private const val DEFAULT_INTERVAL: Long = 15 * 1000
        private val DEFAULT_PERIOD = ChartPeriod.DAY_1
    }
}
