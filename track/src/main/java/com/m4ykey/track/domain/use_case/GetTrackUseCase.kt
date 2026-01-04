package com.m4ykey.track.domain.use_case

import androidx.paging.PagingData
import com.m4ykey.track.data.local.model.TrackEntity
import com.m4ykey.track.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetTrackUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(id : String) : Flow<PagingData<TrackEntity>> {
        return withContext(Dispatchers.IO) {
            repository.getTrackById(id)
        }
    }
}