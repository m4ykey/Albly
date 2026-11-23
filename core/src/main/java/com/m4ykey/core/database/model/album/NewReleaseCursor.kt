package com.m4ykey.core.database.model.album

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_release_cursor")
data class NewReleaseCursor(
    @PrimaryKey val id : Int = 0,
    val fetchedAt : Long,
    val nextPage : Int?
)
