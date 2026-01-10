package com.m4ykey.album.domain.repository

import androidx.paging.PagingData
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.AlbumWithStates
import com.m4ykey.album.data.local.model.IsAlbumSaved
import com.m4ykey.album.data.local.model.IsListenLaterSaved
import com.m4ykey.album.domain.model.AlbumDetail
import com.m4ykey.core.model.domain.AlbumItem
import com.m4ykey.core.model.type.AlbumType
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun getAlbumById(id : String) : Flow<AlbumDetail>
    fun getNewRelease(offset : Int, limit : Int) : Flow<PagingData<AlbumItem>>

    suspend fun insertAlbum(album : AlbumEntity)
    suspend fun insertSavedAlbum(isAlbumSaved: IsAlbumSaved)
    suspend fun insertListenLaterSaved(isListenLaterSaved: IsListenLaterSaved)
    fun getLocalAlbumById(id : String) : AlbumEntity?
    fun getSavedAlbumState(id : String) : IsAlbumSaved?
    fun getSavedListenLaterState(id : String) : IsListenLaterSaved?
    suspend fun getAlbumWithStates(id : String) : AlbumWithStates?
    suspend fun deleteAlbum(id : String)
    suspend fun deleteSavedListenLaterState(id : String)
    suspend fun deleteSavedAlbumState(id : String)
    fun getSavedAlbums(query : String, type : AlbumType?) : Flow<List<AlbumEntity>>
    suspend fun getListenLaterAlbums() : List<AlbumEntity>
    suspend fun getRandomAlbum() : Flow<AlbumEntity>
}