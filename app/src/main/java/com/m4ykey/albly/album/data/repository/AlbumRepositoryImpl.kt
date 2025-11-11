package com.m4ykey.albly.album.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.album.data.paging.TrackPagingSource
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.TrackItem
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.auth.token.getToken
import com.m4ykey.albly.util.network.safeApi
import com.m4ykey.albly.util.paging.pagingConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val service : AlbumService,
    private val dispatcherIO : CoroutineDispatcher,
    private val token : SpotiTokenProvider
) : AlbumRepository {

    override fun getAlbumTracks(
        id: String,
        offset: Int,
        limit: Int
    ): Flow<PagingData<TrackItem>> {
        return Pager(
            pagingSourceFactory = {
                TrackPagingSource(
                    id = id,
                    token = token,
                    service = service
                )
            },
            config = pagingConfig
        ).flow.flowOn(dispatcherIO)
    }

    override suspend fun getAlbumById(id: String): Flow<AlbumDetail> {
        return flow {
            val result = safeApi {
                service.getAlbumById(
                    token = getToken(token),
                    id = id
                ).toDomain()
            }

            result.fold(
                onSuccess = { emit(it) },
                onFailure = { throw it }
            )
        }.flowOn(dispatcherIO)
    }
}