package com.m4ykey.albly.album.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithStates(
    @Embedded val albumEntity: AlbumEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val isAlbumSaved: IsAlbumSaved?,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val isListenLaterSaved: IsListenLaterSaved?
)