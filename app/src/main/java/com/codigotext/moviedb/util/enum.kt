package com.codigotext.moviedb.util

enum class MOVIE_TYPE(value: String) {
    upcoming("upcoming"),
    populator("populator")
}

enum class RefreshModel {
    FORCE, NORMAL
}

enum class INTENT_KEY(value: String) {
    DATA("data"), ID("id")

}