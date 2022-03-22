package com.cosmos.candelabra.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmos.candelabra.data.local.mapper.SearchMapper
import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.model.SearchItem
import com.cosmos.candelabra.data.repository.YahooFinanceRepository
import com.cosmos.candelabra.util.extension.updateValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val yahooFinanceRepository: YahooFinanceRepository
) : ViewModel() {

    private val _query: MutableStateFlow<String?> = MutableStateFlow(null)
    val query: StateFlow<String?> get() = _query

    val items: Flow<Resource<List<SearchItem>>> = _query.transform { query ->
        if (!query.isNullOrBlank()) {
            yahooFinanceRepository.search(query).collect { emit(it) }
        }
    }.map {
        when (it) {
            is Resource.Success -> Resource.Success(SearchMapper.dataToEntities(it.data!!))
            is Resource.Loading -> Resource.Loading()
            is Resource.Error -> Resource.Error(it.message, it.code)
        }
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    fun setQuery(query: String) {
        _query.updateValue(query)
    }

    fun forceRefresh() {
        // TODO: Find a better way to do that
        val oldValue = _query.value
        _query.value = null
        _query.value = oldValue
    }
}
