package com.m4ykey.search.domain.repository

import androidx.paging.PagingData
import com.m4ykey.search.domain.model.search.ResultsAlbum
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchAlbum(query : String, page : Int, perPage : Int) : Flow<PagingData<ResultsAlbum>>
    //fun searchArtist(q : String, offset : Int, limit : Int, type : String) : Flow<PagingData<ArtistItem>>

}