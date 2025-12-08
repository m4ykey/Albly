package com.m4ykey.lyrics.data.mapper

import com.m4ykey.lyrics.data.model.LyricsItem
import com.m4ykey.lyrics.data.network.dto.LyricsDtoItem

fun LyricsDtoItem.toDomain() : LyricsItem {
    return LyricsItem(
        id = id ?: 0,
        name = name.orEmpty(),
        albumName = albumName.orEmpty(),
        artistName = artistName.orEmpty(),
        duration = duration ?: 0.0,
        instrumental = instrumental ?: false,
        plainLyrics = plainLyrics.orEmpty(),
        syncedLyrics = syncedLyrics.orEmpty(),
        trackName = trackName.orEmpty()
    )
}