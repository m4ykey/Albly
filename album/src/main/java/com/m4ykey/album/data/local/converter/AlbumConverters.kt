package com.m4ykey.album.data.local.converter

import androidx.room.TypeConverter
import com.m4ykey.album.data.database.decode
import com.m4ykey.album.data.database.encode
import com.m4ykey.album.data.local.model.ArtistEntity
import com.m4ykey.album.data.local.model.TrackEntity

class AlbumConverters {

    @TypeConverter
    fun fromArtistList(value : List<ArtistEntity>) : String = encode(value)

    @TypeConverter
    fun toArtistList(value : String) : List<ArtistEntity> = decode(value)

    @TypeConverter
    fun fromTrackList(value : List<TrackEntity>) : String = encode(value)

    @TypeConverter
    fun toTrackList(value : String) : List<TrackEntity> = decode(value)

}