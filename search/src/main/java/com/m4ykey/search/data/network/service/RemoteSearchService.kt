package com.m4ykey.search.data.network.service

import java.util.Locale

interface RemoteSearchService {

    suspend fun searchAlbum(
        q : String,
        offset : Int,
        limit : Int,
        type : String,
        market : String = Locale.getDefault().country.ifBlank { "US" }
    ) : AlbumListDto

    suspend fun searchArtist(
        q : String,
        type : String,
        limit : Int,
        offset : Int,
        market : String = Locale.getDefault().country.ifBlank { "US" }
    ) : ArtistListDto

}