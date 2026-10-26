package com.m4ykey.lyrics.data.mapper

import com.m4ykey.lyrics.data.dto.LyricsDtoItem
import com.m4ykey.lyrics.data.dto.LyricsRedItemDto
import com.m4ykey.lyrics.domain.model.LyricsItem
import com.m4ykey.lyrics.domain.model.LyricsRedItem

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

fun LyricsRedItemDto.toDomain() : LyricsRedItem {
    return LyricsRedItem(
        title = title.orEmpty(),
        artist = artist.orEmpty(),
        color = color.orEmpty(),
        album = album.orEmpty(),
        cover = cover.orEmpty(),
        duration = duration ?: 0.0,
        year = year ?: 0,
        isrc = isrc.orEmpty()
    )
}