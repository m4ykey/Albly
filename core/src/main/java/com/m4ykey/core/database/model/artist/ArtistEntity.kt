package com.m4ykey.core.database.model.artist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_table")
data class ArtistEntity(
    @PrimaryKey(autoGenerate = false) val id : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "uri") val uri : String,
    @ColumnInfo(name = "images") val images : List<ImageEntity>,
    @ColumnInfo(name = "realname") val realname : String,
    @ColumnInfo(name = "profile") val profile : String,
    @ColumnInfo(name = "urls") val urls : List<String>,
    @ColumnInfo(name = "save_time") val save_time : Long? = null
)
