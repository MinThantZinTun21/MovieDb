package com.codigotext.moviedb.util

import androidx.room.TypeConverter

class GenreConverter {
    @TypeConverter
    fun getListFromString(genId: String): List<Int> {
        val list = mutableListOf<Int>()
        val array = genId.split(",".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()

        for (s in array) {
            if (s.isNotEmpty()) {
                list.add(s.toInt())
            }
        }
        return list
    }
}