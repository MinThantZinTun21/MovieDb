package com.codigotext.moviedb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remotekey")
class RemoteKey(
    @PrimaryKey
    val id: Int,
    val previousKey:Int?,
    val nextPageKey: Int?,

)