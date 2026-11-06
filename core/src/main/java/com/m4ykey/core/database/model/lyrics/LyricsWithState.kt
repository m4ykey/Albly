package com.m4ykey.core.database.model.lyrics

import androidx.room.Embedded
import androidx.room.Relation

data class LyricsWithState(
    @Embedded val lyricsEntity: LyricsEntity,
    @Relation(
        parentColumn = "track_key",
        entityColumn = "track_key"
    )
    val isLyricsSaved: IsLyricsSaved?
)
