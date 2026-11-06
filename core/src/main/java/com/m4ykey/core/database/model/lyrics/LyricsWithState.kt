package com.m4ykey.core.database.model.lyrics

import androidx.room.Embedded
import androidx.room.Relation

data class LyricsWithState(
    @Embedded val lyricsEntity: LyricsEntity,
    @Relation(
        parentColumn = "remote_id",
        entityColumn = "remote_id"
    )
    val isLyricsSaved: IsLyricsSaved?
)
