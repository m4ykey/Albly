package com.m4ykey.track.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.m4ykey.core.model.local.ArtistEntity
import com.m4ykey.core.model.local.ExternalUrlsEntity

@Entity(
    tableName = "track_table",
    indices = [Index(value = ["albumId"])]
)
data class TrackEntity(
    @ColumnInfo(name = "artists") val artists : List<ArtistEntity>,
    @ColumnInfo(name = "disc_number") val discNumber : Int,
    @ColumnInfo(name = "duration_ms") val durationMs : Int,
    @ColumnInfo(name = "explicit") val explicit : Boolean,
    @ColumnInfo(name = "external_urls") val externalUrls : ExternalUrlsEntity,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "preview_url") val previewUrl : String?,
    @ColumnInfo(name = "track_number") val trackNumber : Int,
    @ColumnInfo(name = "albumId") val albumId : String
)