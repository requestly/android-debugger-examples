package com.cosmos.candelabra.data.repository

import com.cosmos.candelabra.data.local.StockDatabase
import com.cosmos.candelabra.data.local.mapper.ChartMapper
import com.cosmos.candelabra.data.local.mapper.QuoteMapper
import com.cosmos.candelabra.data.model.Chart
import com.cosmos.candelabra.data.model.ChartPeriod
import com.cosmos.candelabra.data.model.ChartWithQuote
import com.cosmos.candelabra.data.model.MarketState
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.db.Quote
import com.cosmos.candelabra.data.remote.api.yahoofinance.YahooFinanceApi
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Charts
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Quotes
import com.cosmos.candelabra.data.remote.api.yahoofinance.model.Search
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class YahooFinanceRepository @Inject constructor(
    private val yahooFinanceApi: YahooFinanceApi,
    private val stockDatabase: StockDatabase
) : BaseRepository() {

    fun getQuote(symbol: String): Flow<Resource<Quotes>> {
        return getQuotes(Collections.singletonList(symbol))
    }

    suspend fun getQuoteSync(symbol: String): Resource<Quotes> {
        return getQuotesSync(Collections.singletonList(symbol))
    }

    fun getQuotes(symbols: List<String>): Flow<Resource<Quotes>> {
        val symbolsQuery = symbols.joinToString(",")
        return safeApiCall { yahooFinanceApi.getQuotes(symbolsQuery) }
    }

    suspend fun getQuotesSync(symbols: List<String>): Resource<Quotes> {
        val symbolsQuery = symbols.joinToString(",")
        return safeApiCallSync { yahooFinanceApi.getQuotes(symbolsQuery) }
    }

    fun getQuoteInRealTime(
        symbol: String,
        interval: Long,
        externalScope: CoroutineScope = GlobalScope,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<Quote>> = flow {
        do {
            var tradeable = false
            val result = getQuoteSync(symbol)

            when (result) {
                is Resource.Success -> {
                    val quotesResult = result.data!!.quoteResponse.result.firstOrNull()

                    if (quotesResult == null) {
                        emit(Resource.Error())
                        return@flow
                    }

                    val data = QuoteMapper.dataToEntity(quotesResult)

                    emit(Resource.Success(data))

                    // Check if at least one quote is tradeable (i.e. market is open)
                    tradeable = (quotesResult.marketState == MarketState.REGULAR.value)
                }
                is Resource.Loading -> emit(Resource.Loading<Quote>())
                is Resource.Error -> {
                    emit(Resource.Error<Quote>(result.message, result.code))
                }
            }

            delay(interval)
        } while (result is Resource.Success && tradeable)
    }.flowOn(coroutineContext)

    fun getQuotesInRealTime(
        quotes: List<Quote>,
        interval: Long,
        externalScope: CoroutineScope = GlobalScope,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<List<Quote>>> = flow {
        val symbols = quotes.map { it.symbol }

        if (symbols.isEmpty()) {
            emit(Resource.Success<List<Quote>>(emptyList()))
            return@flow
        }

        do {
            var tradeable = false
            val result = getQuotesSync(symbols)

            when (result) {
                is Resource.Success -> {
                    val quotesResult = result.data!!.quoteResponse.result
                    val data = QuoteMapper.dataToEntities(quotesResult)

                    externalScope.launch(coroutineContext) {
                        data.forEach {
                            stockDatabase.quoteDao().update(it)
                        }
                    }

                    emit(Resource.Success(data))

                    // Check if at least one quote is tradeable (i.e. market is open)
                    tradeable = quotesResult.find {
                        it.marketState == MarketState.REGULAR.value
                    } != null
                }
                is Resource.Loading -> emit(Resource.Loading<List<Quote>>())
                is Resource.Error -> {
                    emit(Resource.Error<List<Quote>>(result.message, result.code))
                }
            }

            delay(interval)
        } while (result is Resource.Success && tradeable)
    }.flowOn(coroutineContext)

    fun getChart(symbol: String, chartPeriod: ChartPeriod): Flow<Resource<Charts>> {
        return safeApiCall {
            yahooFinanceApi.getChart(symbol, chartPeriod.range, chartPeriod.interval)
        }
    }

    suspend fun getChartSync(symbol: String, chartPeriod: ChartPeriod): Resource<Charts> {
        return safeApiCallSync {
            yahooFinanceApi.getChart(symbol, chartPeriod.range, chartPeriod.interval)
        }
    }

    fun getChartInRealTime(
        symbol: String,
        chartPeriod: ChartPeriod,
        interval: Long,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<Chart>> = flow {
        do {
            var tradeable = false
            val result = getChartSync(symbol, chartPeriod)

            when (result) {
                is Resource.Success -> {
                    val data = ChartMapper.dataToEntity(result.data!!)

                    if (data == null) {
                        emit(Resource.Error())
                        return@flow
                    }

                    emit(Resource.Success(data))

                    tradeable = result.data.chart.result.first().meta.currentTradingPeriod.regular
                        .inPeriod
                }
                is Resource.Loading -> emit(Resource.Loading<Chart>())
                is Resource.Error -> {
                    emit(Resource.Error<Chart>(result.message, result.code))
                }
            }

            delay(interval)
        } while (result is Resource.Success && tradeable)
    }.flowOn(coroutineContext)

    fun getChartsInRealTime(
        charts: List<ChartWithQuote>,
        chartPeriod: ChartPeriod,
        interval: Long,
        externalScope: CoroutineScope = GlobalScope,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Flow<Resource<List<ChartWithQuote>>> = flow {
        val symbols = charts.map { it.chart.symbol }

        if (symbols.isEmpty()) {
            emit(Resource.Success<List<ChartWithQuote>>(emptyList()))
            return@flow
        }

        do {
            val apiScope = CoroutineScope(coroutineContext)

            // Fetch data for all quotes
            val quotesDataAsync = apiScope.async { getQuotesSync(symbols) }

            // Fetch chart data for each quote
            val chartsDataAsync = charts.map {
                apiScope.async {
                    val chart = getChartSync(it.chart.symbol, chartPeriod)
                    mapChart(chart)
                }
            }

            val quotesData = quotesDataAsync.await()
            val chartsData = chartsDataAsync.awaitAll().filterNotNull()

            var hasFetchFailed = false
            var tradeable = false

            when (quotesData) {
                is Resource.Success -> {
                    val quotesResult = quotesData.data!!.quoteResponse.result
                    val quoteEntities = QuoteMapper.dataToEntities(quotesResult)

                    // Update quotes in database
                    externalScope.launch(coroutineContext) {
                        quoteEntities.forEach {
                            stockDatabase.quoteDao().update(it)
                        }
                    }

                    // Map charts with quotes
                    val newChartsAsync = coroutineScope {
                        chartsData.map { chart ->
                            async(coroutineContext) { mapChart(chart, quoteEntities) }
                        }
                    }

                    // Filter out charts that might not have been successful
                    val newCharts = newChartsAsync.awaitAll().filterNotNull()
                    emit(Resource.Success(newCharts))

                    tradeable = quotesResult.find {
                        it.marketState == MarketState.REGULAR.value
                    } != null
                }
                else -> {
                    hasFetchFailed = true
                    emit(Resource.Error<List<ChartWithQuote>>())
                }
            }

            delay(interval)
        } while (!hasFetchFailed && tradeable)
    }.flowOn(coroutineContext)

    private fun mapChart(chart: Chart, quotes: List<Quote>): ChartWithQuote? {
        val quote = quotes.find { it.symbol == chart.symbol }
        return quote?.let {
            ChartWithQuote(chart, it)
        }
    }

    private fun mapChart(chart: Resource<Charts>): Chart? {
        return when (chart) {
            is Resource.Success -> {
                return ChartMapper.dataToEntity(chart.data!!)
            }
            else -> null
        }
    }

    fun search(query: String): Flow<Resource<Search>> {
        return safeApiCall { yahooFinanceApi.search(query) }
    }
}
