package com.m4ykey.artist.data.repository

import com.m4ykey.artist.data.mapper.toArtist
import com.m4ykey.artist.data.service.RemoteArtistService
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.network.safeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArtistRepositoryImpl(
    private val service : RemoteArtistService
) : ArtistRepository {

    override suspend fun getArtistById(id: Int): Flow<Artist> = flow {
        val result = safeApi {
            service.getArtistById(id)
        }.map { it.toArtist() }

        result.fold(
            onSuccess = { item ->
                emit(item)
            },
            onFailure = { error ->
                throw error
            }
        )
    }
}