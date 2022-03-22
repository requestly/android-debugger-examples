package com.cosmos.candelabra.data.repository

import com.cosmos.candelabra.data.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext

abstract class BaseRepository {

    fun <T> safeApiCall(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        apiCall: suspend () -> T
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading<T>())

        runCatching {
            emit(Resource.Success(apiCall.invoke()))
        }.onFailure {
            emit(parseError<T>(it))
        }
    }.flowOn(coroutineContext)

    suspend fun <T> safeApiCallSync(apiCall: suspend () -> T): Resource<T> {
        var result: Resource<T> = Resource.Error()

        runCatching {
            result = Resource.Success(apiCall.invoke())
        }.onFailure {
            result = parseError(it)
        }

        return result
    }

    private fun <T> parseError(throwable: Throwable): Resource<T> {
        return when (throwable) {
            is IOException -> Resource.Error(throwable.message)
            is HttpException -> Resource.Error(throwable.message(), throwable.code())
            else -> Resource.Error()
        }
    }
}
