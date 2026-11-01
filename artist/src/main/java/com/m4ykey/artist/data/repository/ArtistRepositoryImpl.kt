package com.m4ykey.artist.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.artist.data.mapper.toArtist
import com.m4ykey.artist.data.paging.ArtistReleasePagingSource
import com.m4ykey.artist.data.service.RemoteArtistService
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.pagingConfig
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

    override fun getArtistReleases(id: Int): Flow<PagingData<ReleaseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                ArtistReleasePagingSource(service = service, id = id)
            }
        ).flow
    }
}