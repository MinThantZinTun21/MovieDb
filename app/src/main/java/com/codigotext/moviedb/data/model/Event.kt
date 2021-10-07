package com.codigotext.moviedb.data.model

sealed class Event {
    data class showErrorMessage(val error: Throwable) : Event()
}