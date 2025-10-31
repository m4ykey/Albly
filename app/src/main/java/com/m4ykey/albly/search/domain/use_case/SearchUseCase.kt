package com.m4ykey.albly.search.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    operator fun invoke(limit : Int = 20, offset : Int = 0, q : String, type : String) : Flow<PagingData<AlbumItem>> {
        return repository.search(
            limit = limit,
            offset = offset,
            q = q,
            type = type
        )
    }

}