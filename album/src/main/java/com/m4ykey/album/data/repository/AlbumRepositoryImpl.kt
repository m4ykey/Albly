package com.m4ykey.album.data.repository

import com.m4ykey.album.data.local.dao.AlbumDao
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.AlbumWithStates
import com.m4ykey.album.data.local.model.IsAlbumSaved
import com.m4ykey.album.data.local.model.IsListenLaterSaved
import com.m4ykey.album.data.network.service.RemoteAlbumService
import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.core.model.type.AlbumType
import kotlinx.coroutines.flow.Flow

class AlbumRepositoryImpl(
    //private val service : RemoteAlbumService,
    private val dao : AlbumDao
) : AlbumRepository {

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