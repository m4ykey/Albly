package com.m4ykey.core.database.model.album

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_release_table")
data class NewReleaseEntity(
    @PrimaryKey val masterId : Int,
    val title : String,
    val thumb : String,
    val coverImage : String,
    val artist : String,
    val position : Int,
    val fetchedAt : Long,
    val year : Int
)
