package com.cosmos.candelabra.data.repository

import com.cosmos.candelabra.data.model.Resource
import com.cosmos.candelabra.data.remote.api.autoc.AutocApi
import com.cosmos.candelabra.data.remote.api.autoc.model.Search
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutocRepository @Inject constructor(private val autocApi: AutocApi) : BaseRepository() {

    fun search(query: String): Flow<Resource<Search>> {
        return safeApiCall { autocApi.search(query) }
    }
}
