package com.m4ykey.albly.album.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m4ykey.core.model.local.ArtistEntity
import com.m4ykey.core.model.local.ExternalUrlsEntity

@Entity(tableName = "album_table")
data class AlbumEntity(
    @ColumnInfo("album_type") val albumType : String,
    @ColumnInfo("artists") val artists : List<ArtistEntity>,
    @ColumnInfo("copyrights") val copyrights : List<CopyrightEntity>,
    @ColumnInfo("external_urls") val externalUrls : ExternalUrlsEntity,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id") val id : String,
    @ColumnInfo("val images") val images : List<ImageEntity>,
    @ColumnInfo("label") val label : String,
    @ColumnInfo("name") val name : String,
    @ColumnInfo("release_date") val releaseDate : String,
    @ColumnInfo("total_tracks") val totalTracks : Int,
    @ColumnInfo("save_time") val saveTime : Long? = null
)
