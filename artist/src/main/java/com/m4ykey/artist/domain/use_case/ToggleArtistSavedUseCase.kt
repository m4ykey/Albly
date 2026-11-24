package com.m4ykey.artist.domain.use_case

import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.database.model.artist.ArtistEntity
import com.m4ykey.core.database.model.artist.IsArtistSaved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToggleArtistSavedUseCase(
    private val repository : ArtistRepository
) {
    suspend operator fun invoke(
        artist : ArtistEntity,
        isCurrentlySaved : Boolean
    ) = withContext(Dispatchers.IO) {
        if (isCurrentlySaved) {
            repository.deleteSavedArtistState(artist.id)
            repository.deleteArtist(artist.id)
        } else {
            repository.insertArtist(artist)
            repository.insertSavedArtist(
                IsArtistSaved(
                    id = artist.id,
                    isArtistSaved = true
                )
            )
        }
    }
}