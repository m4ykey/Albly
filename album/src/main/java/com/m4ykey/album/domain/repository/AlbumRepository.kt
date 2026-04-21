package com.m4ykey.album.domain.repository

import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.AlbumWithStates
import com.m4ykey.album.data.local.model.IsAlbumSaved
import com.m4ykey.album.data.local.model.IsListenLaterSaved
import com.m4ykey.album.domain.model.AlbumRoot
import com.m4ykey.core.model.type.AlbumType
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun getAlbum(id : Int) : Flow<AlbumRoot>

    suspend fun insertAlbum(album : AlbumEntity)
    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved)
    suspend fun insertListenLaterSaved(isListenLaterSaved: IsListenLaterSaved)
    fun getLocalAlbumById(id : Int) : AlbumEntity?
    fun getSavedAlbumState(id : Int) : IsAlbumSaved?
    fun getSavedListenLaterState(id : Int) : IsListenLaterSaved?
    suspend fun getAlbumWithStates(id : Int) : AlbumWithStates?
    suspend fun deleteAlbum(id : Int)
    suspend fun deleteSavedListenLaterState(id : Int)
    suspend fun deleteSavedAlbumState(id : Int)
    fun getSavedAlbums(query : String) : Flow<List<AlbumEntity>>
    suspend fun getListenLaterAlbums() : List<AlbumEntity>
    suspend fun getRandomAlbum() : Flow<AlbumEntity>
}