package com.m4ykey.artist.domain.repository

import com.m4ykey.artist.domain.model.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    suspend fun getArtistById(id : Int) : Flow<Artist>

}