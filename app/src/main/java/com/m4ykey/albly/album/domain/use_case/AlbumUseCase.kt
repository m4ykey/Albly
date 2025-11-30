package com.m4ykey.albly.album.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class AlbumUseCase (
    private val repository : AlbumRepository
) {

    fun getNewRelease(limit : Int = 20, offset : Int = 0) : Flow<PagingData<AlbumItem>> {
        return repository.getNewRelease(offset = offset, limit = limit)
    }

    suspend fun albumId(id : String) : Flow<AlbumDetail> {
        return repository.getAlbumById(id)
    }

    fun trackId(id : String, limit : Int = 20, offset : Int = 0) : Flow<PagingData<TrackItem>> {
        return repository.getAlbumTracks(id = id, offset = offset, limit = limit)
    }

}