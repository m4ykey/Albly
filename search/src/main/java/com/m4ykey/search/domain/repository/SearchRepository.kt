package com.m4ykey.search.domain.repository

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.search.album.ResultsAlbum
import com.m4ykey.search.domain.model.search.artist.ResultsArtist
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchAlbum(query : String, page : Int, perPage : Int) : Flow<PagingData<ResultsAlbum>>
    fun searchArtist(query : String, page : Int, perPage: Int) : Flow<PagingData<ResultsArtist>>

}