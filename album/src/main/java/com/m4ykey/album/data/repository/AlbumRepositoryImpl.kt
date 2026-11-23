@file:OptIn(ExperimentalPagingApi::class)

package com.m4ykey.album.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.m4ykey.album.data.mapper.toAlbumRoot
import com.m4ykey.album.data.mapper.toModel
import com.m4ykey.album.data.network.service.RemoteAlbumService
import com.m4ykey.album.data.network.service.RemoteNewReleaseAlbumService
import com.m4ykey.album.data.remote_mediator.NewReleaseRemoteMediator
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.database.dao.AlbumDao
import com.m4ykey.core.database.dao.NewReleaseDao
import com.m4ykey.core.database.model.album.AlbumEntity
import com.m4ykey.core.database.model.album.AlbumListItem
import com.m4ykey.core.database.model.album.AlbumWithStates
import com.m4ykey.core.database.model.album.IsAlbumSaved
import com.m4ykey.core.database.model.album.IsListenLaterSaved
import com.m4ykey.core.database.model.album.NewReleaseEntity
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.pagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class AlbumRepositoryImpl(
    private val service : RemoteAlbumService,
    private val dao : AlbumDao,
    private val newReleaseService : RemoteNewReleaseAlbumService,
    private val newReleaseDao: NewReleaseDao
) : AlbumRepository {

    private val MAX_CACHED_ALBUMS = 200

    override suspend fun getAlbum(id: Int): Flow<AlbumRoot> = flow {
        val response = safeApi { service.getAlbum(id) }


        response.fold(
            onSuccess = { item ->
                emit(item.toAlbumRoot())
            },
            onFailure = { error ->
                throw error
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getNewRelease(): Flow<PagingData<NewReleaseResult>> {
        val year = LocalDate.now().year

        return Pager(
            config = pagingConfig,
            remoteMediator = NewReleaseRemoteMediator(
                dao = newReleaseDao,
                service = newReleaseService,
                year = year,
                perPage = pagingConfig.pageSize
            ),
            pagingSourceFactory = { newReleaseDao.pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toModel() }
        }
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

    override fun getSavedAlbumState(id: Int): IsAlbumSaved? {
        return dao.getSavedAlbumState(id)
    }

    override fun getSavedListenLaterState(id: Int): IsListenLaterSaved? {
        return dao.getSavedListenLaterState(id)
    }

    override suspend fun getAlbumWithStates(id: Int): AlbumWithStates? {
        return dao.getAlbumWithStates(id)
    }

    override suspend fun deleteAlbum(id: Int) {
        dao.deleteAlbum(id)
    }

    override suspend fun deleteSavedListenLaterState(id: Int) {
        dao.deleteSavedListenLaterState(id)
    }

    override suspend fun deleteSavedAlbumState(id: Int) {
        dao.deleteSavedAlbumState(id)
    }

    override fun getSavedAlbumsLatest(query : String): Flow<List<AlbumListItem>> {
        return dao.getSavedAlbumsLatest(query)
    }

    override fun getSavedAlbumsOldest(query: String): Flow<List<AlbumListItem>> {
        return dao.getSavedAlbumsOldest(query)
    }

    override fun getSavedAlbumsAlphabetical(query: String): Flow<List<AlbumListItem>> {
        return dao.getSavedAlbumsAlphabetical(query)
    }

    override suspend fun getListenLaterAlbums(): List<AlbumListItem> {
        return dao.getListenLaterAlbums()
    }

    override suspend fun getRandomAlbum(): Flow<AlbumEntity> {
        return dao.getRandomAlbum()
    }

    override suspend fun cacheAlbum(album: AlbumEntity) {
        if (dao.isAlbumStored(album.id)) return

        dao.insertAlbum(album)

        val cached = dao.countCachedAlbums()
        if (cached > MAX_CACHED_ALBUMS) {
            dao.deleteOldestCachedAlbums(cached - MAX_CACHED_ALBUMS)
        }
    }
}