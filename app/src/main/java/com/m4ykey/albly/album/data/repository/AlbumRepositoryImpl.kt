package com.m4ykey.albly.album.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.album.data.local.dao.AlbumDao
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.album.data.local.model.AlbumWithStates
import com.m4ykey.albly.album.data.local.model.IsAlbumSaved
import com.m4ykey.albly.album.data.local.model.IsListenLaterSaved
import com.m4ykey.albly.album.data.network.service.RemoteAlbumService
import com.m4ykey.albly.album.data.paging.NewReleasePaging
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.AlbumItem
import com.m4ykey.albly.album.domain.repository.AlbumRepository
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.core.mapper.toDomain
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.pagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AlbumRepositoryImpl(
    private val service : RemoteAlbumService,
    private val dao : AlbumDao
) : AlbumRepository {

    override fun getNewRelease(
        offset: Int,
        limit: Int
    ): Flow<PagingData<AlbumItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                NewReleasePaging(service = service)
            }
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
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertAlbum(album: AlbumEntity) {
        dao.insertAlbum(album)
    }

    override suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved) {
        dao.insertSavedAlbum(isAlbumSaved)
    }

    override suspend fun insertListenLaterSaved(isListenLaterSaved: IsListenLaterSaved) {
        dao.insertListenLaterSaved(isListenLaterSaved)
    }

    override fun getLocalAlbumById(id: String): AlbumEntity? {
        return dao.getAlbumById(id)
    }

    override fun getSavedAlbumState(id: String): IsAlbumSaved? {
        return dao.getSavedAlbumState(id)
    }

    override fun getSavedListenLaterState(id: String): IsListenLaterSaved? {
        return dao.getSavedListenLaterState(id)
    }

    override suspend fun getAlbumWithStates(id: String): AlbumWithStates? {
        return dao.getAlbumWithStates(id)
    }

    override suspend fun deleteAlbum(id: String) {
        dao.deleteAlbum(id)
    }

    override suspend fun deleteSavedListenLaterState(id: String) {
        dao.deleteSavedListenLaterState(id)
    }

    override suspend fun deleteSavedAlbumState(id: String) {
        dao.deleteSavedAlbumState(id)
    }

    override fun getSavedAlbums(query : String, type : AlbumType?): Flow<List<AlbumEntity>> {
        return dao.getSavedAlbums(query, type?.name)
    }

    override suspend fun getListenLaterAlbums(): List<AlbumEntity> {
        return dao.getListenLaterAlbums()
    }

    override suspend fun getRandomAlbum(): Flow<AlbumEntity> {
        return dao.getRandomAlbum()
    }
}