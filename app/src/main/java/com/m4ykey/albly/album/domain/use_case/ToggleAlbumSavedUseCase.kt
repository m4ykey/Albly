package com.m4ykey.albly.album.domain.use_case

import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.IsAlbumSaved
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.track.data.local.model.TrackEntity
import com.m4ykey.track.domain.use_case.DeleteTracksUseCase
import com.m4ykey.track.domain.use_case.SaveTracksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToggleAlbumSavedUseCase(
    private val repository : AlbumRepository,
    private val saveTracksUseCase: SaveTracksUseCase,
    private val deleteTracksUseCase: DeleteTracksUseCase
) {
    suspend operator fun invoke(
        album : AlbumEntity,
        isCurrentlySaved : Boolean,
        tracks : List<TrackEntity>
    ) = withContext(Dispatchers.IO) {
        if (isCurrentlySaved) {
            repository.deleteSavedAlbumState(album.id)

            val state = repository.getAlbumWithStates(id = album.id)
            if (state?.isListenLaterSaved == null) {
                repository.deleteAlbum(album.id)
                deleteTracksUseCase(album.id)
            }
        } else {
            saveTracksUseCase(tracks)
            repository.insertAlbum(album)
            repository.insertSavedAlbum(IsAlbumSaved(id = album.id, isAlbumSaved = true))
        }
    }
}