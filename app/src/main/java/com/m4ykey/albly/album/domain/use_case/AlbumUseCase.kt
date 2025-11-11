package com.m4ykey.albly.album.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumUseCase @Inject constructor(
    private val repository : AlbumRepository
) {

    suspend fun albumId(id : String) : Flow<AlbumDetail> {
        return repository.getAlbumById(id)
    }

    fun trackId(id : String, limit : Int = 20, offset : Int = 0) : Flow<PagingData<TrackItem>> {
        return repository.getAlbumTracks(id = id, offset = offset, limit = limit)
    }

}