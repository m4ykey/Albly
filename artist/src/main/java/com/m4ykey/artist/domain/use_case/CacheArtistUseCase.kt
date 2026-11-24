package com.m4ykey.artist.domain.use_case

import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.database.model.artist.ArtistEntity

class CacheArtistUseCase(
    private val repository : ArtistRepository
) {
    suspend operator fun invoke(artist : ArtistEntity) {
        repository.cacheArtist(artist)
    }
}