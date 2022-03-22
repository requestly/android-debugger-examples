package com.cosmos.candelabra.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cosmos.candelabra.data.local.StockDatabase
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
class OverviewViewModel @Inject constructor(
    private val stockDatabase: StockDatabase,
    private val yahooFinanceRepository: YahooFinanceRepository
) : ViewModel() {

    private val _coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO

    val userQuotes: LiveData<List<Quote>> = stockDatabase
        .quoteDao()
        .getAllQuotes()
        .distinctUntilChangedBy { it.size } // Send updates only when a quote is added or removed
        .asLiveData(_coroutineContext)

    fun fetchQuotesInRealTime(
        quotes: List<Quote>? = userQuotes.value,
        interval: Long = DEFAULT_INTERVAL,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<List<Quote>>> {
        quotes?.let { _quotes ->
            return yahooFinanceRepository.getQuotesInRealTime(
                _quotes,
                interval,
                coroutineContext = coroutineContext
            )
        }
        return flow { emit(Resource.Error()) }
    }

    private suspend fun updateQuotes(quotes: List<Quote>) {
        quotes.forEach {
            stockDatabase.quoteDao().update(it)
        }
    }

    companion object {
        private const val DEFAULT_INTERVAL: Long = 10 * 1000
    }
}
