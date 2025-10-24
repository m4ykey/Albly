package com.m4ykey.albly.search.domain.repository

import androidx.paging.PagingData
import com.m4ykey.albly.search.domain.model.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun search(query : String, offset : Int, limit : Int) : Flow<PagingData<Search>>

}