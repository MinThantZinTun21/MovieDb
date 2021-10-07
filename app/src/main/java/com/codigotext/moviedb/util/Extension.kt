package com.codigotext.moviedb.util

fun Boolean.ToBooleanInt(): Int {
    return if (this) {
        1
    } else 0
}

fun Int.ToInBoolean(): Boolean {
    return this==1
}