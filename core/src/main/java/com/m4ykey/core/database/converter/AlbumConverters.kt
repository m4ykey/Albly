package com.m4ykey.core.database.converter

import androidx.room.TypeConverter
import com.m4ykey.core.database.decode
import com.m4ykey.core.database.encode
import com.m4ykey.core.database.model.album.AlbumArtistEntity
import com.m4ykey.core.database.model.album.TrackEntity

class AlbumConverters {

    @TypeConverter
    fun fromArtistList(value : List<AlbumArtistEntity>) : String = encode(value)

    @TypeConverter
    fun toArtistList(value : String) : List<AlbumArtistEntity> = decode(value)

    @TypeConverter
    fun fromTrackList(value : List<TrackEntity>) : String = encode(value)

    @TypeConverter
    fun toTrackList(value : String) : List<TrackEntity> = decode(value)

}