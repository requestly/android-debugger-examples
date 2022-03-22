package com.cosmos.candelabra.ui.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.cosmos.candelabra.data.local.StockDatabase
import com.cosmos.candelabra.data.model.Chart
import com.cosmos.candelabra.data.model.ChartPeriod
import com.cosmos.candelabra.data.model.ChartWithQuote
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.data.repository.YahooFinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    stockDatabase: StockDatabase,
    private val yahooFinanceRepository: YahooFinanceRepository
) : ViewModel() {

    private val _coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    private val _userQuotes: LiveData<List<Quote>> = stockDatabase
        .quoteDao()
        .getAllQuotes()
        .distinctUntilChangedBy { it.size } // Send updates only when a quote is added or removed
        .asLiveData(_coroutineContext)

    val userCharts: LiveData<List<ChartWithQuote>> = _userQuotes.map { quotes ->
        quotes.map { quote ->
            ChartWithQuote(
                Chart(quote.symbol, quote.currency, quote.price, quote.previousClose, emptyList()),
                quote
            )
        }
    }

    fun fetchChartsInRealTime(
        charts: List<ChartWithQuote>? = userCharts.value,
        chartPeriod: ChartPeriod = DEFAULT_PERIOD,
        interval: Long = DEFAULT_INTERVAL,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<List<ChartWithQuote>>> {
        charts?.let { _charts ->
            return yahooFinanceRepository.getChartsInRealTime(
                _charts,
                chartPeriod,
                interval,
                coroutineContext = coroutineContext
            )
        }
        return flow { emit(Resource.Error()) }
    }

    companion object {
        private const val DEFAULT_INTERVAL: Long = 15 * 1000
        private val DEFAULT_PERIOD = ChartPeriod.DAY_1
    }
}
