package com.m4ykey.lyrics.data.mapper

import com.m4ykey.lyrics.data.network.dto.LyricsDtoItem
import com.m4ykey.lyrics.domain.model.LyricsItem

fun LyricsDtoItem.toDomain() : LyricsItem {
    return LyricsItem(
        id = id ?: 0,
        name = name.orEmpty(),
        albumName = albumName.orEmpty(),
        artistName = artistName.orEmpty(),
        plainLyrics = plainLyrics.orEmpty(),
        syncedLyrics = syncedLyrics.orEmpty(),
        trackName = trackName.orEmpty()
    )
}