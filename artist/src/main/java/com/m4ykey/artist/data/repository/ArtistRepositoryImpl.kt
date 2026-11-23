@file:OptIn(ExperimentalPagingApi::class)

package com.m4ykey.artist.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.m4ykey.artist.data.mapper.toArtist
import com.m4ykey.artist.data.mapper.toReleaseItem
import com.m4ykey.artist.data.remote_mediator.ArtistReleaseRemoteMediator
import com.m4ykey.artist.data.service.RemoteArtistService
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.database.dao.ArtistReleaseDao
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.pagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ArtistRepositoryImpl(
    private val service : RemoteArtistService,
    private val dao : ArtistReleaseDao
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
    }.flowOn(Dispatchers.IO)

    override fun getArtistReleases(id: Int): Flow<PagingData<ReleaseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.pagingSource(id) },
            remoteMediator = ArtistReleaseRemoteMediator(
                dao = dao,
                service = service,
                artistId = id,
                perPage = pagingConfig.pageSize
            )
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toReleaseItem() }
        }
    }
}