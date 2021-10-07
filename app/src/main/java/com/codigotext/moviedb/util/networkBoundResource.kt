package com.codigotext.moviedb.util

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> kotlinx.coroutines.flow.Flow<ResultType>,
    crossinline load: suspend () -> RequestType,
    crossinline  saveLoadResult: suspend (RequestType) -> Unit,
    crossinline shouldLoad: (ResultType) -> Boolean = { true },

    crossinline onLoadSuccess: () -> Unit = {},
    crossinline onLoadError: (Throwable) -> Unit = {}
) = channelFlow {
    val data = query().first()
    if (shouldLoad(data)) {
        val loading = launch {
            query().collect {
                send(Resource.Loading(it))
            }
        }
        try {
            saveLoadResult(load())
            onLoadSuccess()
            loading.cancel()
            query().collect {
                send(Resource.Success(it))
            }
        } catch (e: Throwable) {
            onLoadError(e)
            loading.cancel()
            query().collect {
                send(Resource.Error(e, it))
            }
        }

    }else {
        query().collect { send(Resource.Success(it)) }
    }
}