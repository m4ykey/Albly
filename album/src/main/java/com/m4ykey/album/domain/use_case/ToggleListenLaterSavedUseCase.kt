package com.m4ykey.album.domain.use_case

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.IsListenLaterSaved
import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.track.data.local.model.TrackEntity
import com.m4ykey.track.domain.use_case.DeleteTracksUseCase
import com.m4ykey.track.domain.use_case.SaveTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToggleListenLaterSavedUseCase(
    private val repository: AlbumRepository,
    private val saveTracksUseCase: SaveTracksUseCase,
    private val deleteTracksUseCase: DeleteTracksUseCase
) {
    suspend operator fun invoke(
        album : AlbumEntity,
        isCurrentlySaved : Boolean,
        tracks : List<TrackEntity>
    ) = withContext(Dispatchers.IO) {
        if (isCurrentlySaved) {
            repository.deleteSavedListenLaterState(album.id)

            val state = repository.getAlbumWithStates(album.id)
            if (state?.isAlbumSaved == null) {
                repository.deleteAlbum(album.id)
                deleteTracksUseCase(album.id)
            }
        } else {
            saveTracksUseCase(tracks)
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