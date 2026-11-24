package com.m4ykey.core.database.converter

import androidx.room.TypeConverter
import com.m4ykey.core.database.decode
import com.m4ykey.core.database.encode
import com.m4ykey.core.database.model.artist.ImageEntity

class ArtistConverters {

    @TypeConverter
    fun fromImageList(value : List<ImageEntity>) : String = encode(value)

    @TypeConverter
    fun toImageList(value : String) : List<ImageEntity> = decode(value)

    @TypeConverter
    fun fromUrlList(value : List<String>) : String = encode(value)

    @TypeConverter
    fun toUrlList(value : String) : List<String> = decode(value)

}