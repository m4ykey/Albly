package com.m4ykey.album.domain.usecase

import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.model.album.AlbumEntity
import com.m4ykey.core.database.model.album.IsListenLaterSaved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToggleListenLaterSavedUseCase(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(
        album : AlbumEntity,
        isCurrentlySaved : Boolean
    ) = withContext(Dispatchers.IO) {
        if (isCurrentlySaved) {
            repository.deleteSavedListenLaterState(album.id)

            val state = repository.getAlbumWithStates(album.id)
            if (state?.isAlbumSaved == null) {
                repository.deleteAlbum(album.id)
            }
        } else {
            repository.insertAlbum(album)
            repository.insertListenLaterSaved(
                IsListenLaterSaved(
                    id = album.id,
                    isListenLaterSaved = true
                )
            )
        }
    }
}