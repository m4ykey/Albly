package com.m4ykey.core.database.model.artist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_release_cursor")
data class ArtistReleaseCursor(
    @PrimaryKey val artistId : Int,
    val fetchedAt : Long,
    val nextPage : Int?
)
