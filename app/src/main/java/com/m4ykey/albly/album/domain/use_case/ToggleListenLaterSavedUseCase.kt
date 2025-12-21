package com.m4ykey.albly.album.domain.use_case

import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.IsListenLaterSaved
import com.m4ykey.albly.album.domain.repository.AlbumRepository

class ToggleListenLaterSavedUseCase(private val repository: AlbumRepository) {
    suspend operator fun invoke(album : AlbumEntity, isCurrentlySaved : Boolean) {
        if (isCurrentlySaved) {
            repository.deleteSavedListenLaterState(album.id)

            val state = repository.getAlbumWithStates(album.id)
            if (state?.isAlbumSaved == null) {
                repository.deleteAlbum(album.id)
            }
        } else {
            repository.insertAlbum(album)
            repository.insertListenLaterSaved(IsListenLaterSaved(id = album.id, isListenLaterSaved = true))
        }
    }
}