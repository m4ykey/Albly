package com.m4ykey.search.data.network.service

import com.m4ykey.search.data.network.model.dto.album.SearchAlbumRootDto
import com.m4ykey.search.data.network.model.dto.artist.SearchArtistRootDto

interface RemoteSearchService {

    suspend fun searchAlbum(
        query : String,
        format : String = "album",
        perPage : Int = 20,
        page : Int,
        type : String = "master"
    ) : SearchAlbumRootDto

    suspend fun searchArtist(
        query : String,
        perPage : Int = 20,
        page : Int,
        type : String = "artist"
    ) : SearchArtistRootDto
}