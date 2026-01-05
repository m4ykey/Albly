package com.m4ykey.search.domain.repository

import androidx.paging.PagingData
import com.m4ykey.core.model.domain.AlbumItem
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun search(q : String, offset : Int, limit : Int, type : String) : Flow<PagingData<AlbumItem>>

}