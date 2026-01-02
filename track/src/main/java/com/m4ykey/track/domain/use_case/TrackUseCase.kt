package com.m4ykey.track.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.track.domain.model.TrackItem
import com.m4ykey.track.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class TrackUseCase(
    private val repository: TrackRepository
) {

    operator fun invoke(id : String, limit : Int = 20, offset : Int = 0) : Flow<PagingData<TrackItem>> {
        return repository.getAlbumTracks(id = id, offset = offset, limit = limit)
    }

}