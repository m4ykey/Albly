package com.m4ykey.albly.album.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.data.network.service.RemoteAlbumService
import com.m4ykey.albly.album.data.paging.TrackPagingSource
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.util.network.safeApi
import com.m4ykey.albly.util.paging.pagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlbumRepositoryImpl(
    private val service : RemoteAlbumService,
)  : AlbumRepository {

    override fun getAlbumTracks(
        id: String,
        offset: Int,
        limit: Int
    ): Flow<PagingData<TrackItem>> {
        return Pager(
            pagingSourceFactory = {
                TrackPagingSource(
                    id = id,
                    service = service
                )
            },
            config = pagingConfig
        ).flow
    }

    override suspend fun getAlbumById(id: String): Flow<AlbumDetail> {
        return flow {
            val result = safeApi {
                service.getAlbumById(id = id).toDomain()
            }

            result.fold(
                onSuccess = { emit(it) },
                onFailure = { throw it }
            )
        }
    }
}