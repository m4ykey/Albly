package com.m4ykey.albly.album.data.repository

import com.m4ykey.albly.album.data.mapper.toDomain
import com.m4ykey.albly.album.data.network.service.AlbumService
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.auth.token.SpotiTokenProvider
import com.m4ykey.albly.auth.token.getToken
import com.m4ykey.albly.util.network.safeApi
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