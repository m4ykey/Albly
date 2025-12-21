package com.m4ykey.albly.album.domain.use_case

import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.IsAlbumSaved
import com.m4ykey.albly.album.domain.repository.AlbumRepository

class ToggleAlbumSavedUseCase(private val repository : AlbumRepository) {
    suspend operator fun invoke(album : AlbumEntity, isCurrentlySaved : Boolean) {
        if (isCurrentlySaved) {
            repository.deleteSavedAlbumState(album.id)

            val state = repository.getAlbumWithStates(id = album.id)
            if (state?.isListenLaterSaved == null) {
                repository.deleteAlbum(album.id)
            }
        } else {
            repository.insertAlbum(album)
            repository.insertSavedAlbum(IsAlbumSaved(id = album.id, isAlbumSaved = true))
        }
    }
}