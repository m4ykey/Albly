package com.m4ykey.albly.album.data.local.converter

import androidx.room.TypeConverter
import com.m4ykey.albly.album.data.local.model.ArtistEntity
import com.m4ykey.albly.album.data.local.model.CopyrightEntity
import com.m4ykey.albly.album.data.local.model.ExternalUrlsEntity
import com.m4ykey.albly.album.data.local.model.ImageEntity
import com.m4ykey.albly.core.database.decode
import com.m4ykey.albly.core.database.encode

class AlbumConverters {

    @TypeConverter
    fun fromArtistList(value : List<ArtistEntity>) : String = encode(value)

    @TypeConverter
    fun toArtistList(value : String) : List<ArtistEntity> = decode(value)

    @TypeConverter
    fun fromCopyrightList(value : List<CopyrightEntity>) : String = encode(value)

    @TypeConverter
    fun toCopyrightList(value : String) : List<CopyrightEntity> = decode(value)

    @TypeConverter
    fun fromExternalUrls(value : ExternalUrlsEntity) : String = encode(value)

    @TypeConverter
    fun toExternalUrls(value : String) : ExternalUrlsEntity = decode(value)

    @TypeConverter
    fun fromImageList(value : List<ImageEntity>) : String = encode(value)

    @TypeConverter
    fun toImageList(value : String) : List<ImageEntity> = decode(value)

}