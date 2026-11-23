package com.m4ykey.core.database.model.artist

import androidx.room.Entity

@Entity(
    tableName = "artist_release_entity",
    primaryKeys = ["artistId", "releaseType", "releaseId"]
)
data class ArtistReleaseEntity(
    val artistId : Int,
    val releaseType : String,
    val releaseId : Int,
    val title : String,
    val thumb : String,
    val year : Int,
    val resourceUrl : String,
    val position : Int,
    val fetchedAt : Long
)
