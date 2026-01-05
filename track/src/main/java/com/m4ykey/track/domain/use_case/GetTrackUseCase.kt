package com.m4ykey.track.domain.use_case

import com.m4ykey.track.data.local.model.TrackEntity
import com.m4ykey.track.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetTrackUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(id : String) : List<TrackEntity> {
        return withContext(Dispatchers.IO) {
            repository.getTrackById(id)
        }
    }
}