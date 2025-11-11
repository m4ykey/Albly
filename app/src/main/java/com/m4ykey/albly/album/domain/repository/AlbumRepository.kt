package com.m4ykey.albly.album.domain.repository

import androidx.paging.PagingData
import com.m4ykey.albly.album.domain.model.AlbumDetail
import com.m4ykey.albly.album.domain.model.TrackItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun getAlbumById(id : String) : Flow<AlbumDetail>
    fun getAlbumTracks(id : String, offset : Int, limit : Int) : Flow<PagingData<TrackItem>>

}