package com.m4ykey.core.database.model.artist

import androidx.room.Embedded
import androidx.room.Relation

data class ArtistWithStates(
    @Embedded val artistEntity: ArtistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val isArtistSaved: IsArtistSaved?
)
