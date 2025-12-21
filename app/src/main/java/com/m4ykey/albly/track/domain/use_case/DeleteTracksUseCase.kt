package com.m4ykey.albly.track.domain.use_case

import com.m4ykey.albly.track.domain.repository.TrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteTracksUseCase(private val repository: TrackRepository) {
    suspend operator fun invoke(id : String) {
        withContext(Dispatchers.IO) {
            repository.deleteTracksById(id)
        }
    }
}