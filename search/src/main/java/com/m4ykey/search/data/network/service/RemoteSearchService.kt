package com.m4ykey.search.data.network.service

import com.m4ykey.search.data.network.dto.album.SearchAlbumRootDto
import com.m4ykey.search.data.network.dto.artist.SearchArtistRootDto
import com.m4ykey.search.data.network.dto.lyrics.LyricsDto

interface RemoteSearchService {

    suspend fun searchAlbum(
        query : String,
        perPage : Int = 20,
        page : Int = 1
    ) : SearchAlbumRootDto

    suspend fun searchArtist(
        query : String,
        perPage : Int = 20,
        page : Int = 1
    ) : SearchArtistRootDto

    suspend fun searchLyrics(
        query : String,
        after : String?
    ) : LyricsDto

}