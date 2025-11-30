package com.m4ykey.albly.album.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow

class TrackUseCase(
    private val repository: AlbumRepository
) {

    operator fun invoke(id : String, limit : Int = 20, offset : Int = 0) : Flow<PagingData<TrackItem>> {
        return repository.getAlbumTracks(id = id, offset = offset, limit = limit)
    }

}