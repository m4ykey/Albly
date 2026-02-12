package com.m4ykey.search.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchAlbum(q : String, offset : Int, limit : Int, type : String) : Flow<PagingData<AlbumItem>>
    fun searchArtist(q : String, offset : Int, limit : Int, type : String) : Flow<PagingData<ArtistItem>>

}