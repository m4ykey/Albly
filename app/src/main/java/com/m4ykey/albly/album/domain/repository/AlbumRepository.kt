package com.m4ykey.albly.album.domain.repository

import com.m4ykey.albly.album.domain.model.AlbumDetail
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    suspend fun getAlbumById(id : String) : Flow<AlbumDetail>

}