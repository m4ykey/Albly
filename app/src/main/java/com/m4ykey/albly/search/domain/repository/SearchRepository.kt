package com.m4ykey.albly.search.domain.repository

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun search(q : String, offset : Int, limit : Int, type : String) : Flow<PagingData<AlbumItem>>

}