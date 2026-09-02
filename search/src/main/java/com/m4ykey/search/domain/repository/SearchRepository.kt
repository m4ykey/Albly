package com.m4ykey.search.domain.repository

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.model.lyrics.GeniusRoot
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchAlbum(query : String, page : Int, perPage : Int) : Flow<PagingData<ResultsAlbum>>
    fun searchArtist(query : String, page : Int, perPage: Int) : Flow<PagingData<ResultsArtist>>
    fun searchLyrics(query : String) : Flow<List<GeniusRoot>>

}