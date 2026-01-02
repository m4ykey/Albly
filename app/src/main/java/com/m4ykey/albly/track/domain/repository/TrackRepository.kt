package com.m4ykey.albly.track.domain.repository

import androidx.paging.PagingData
import com.m4ykey.albly.track.data.local.model.TrackEntity
import com.m4ykey.albly.track.domain.model.TrackItem
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun getAlbumTracks(id : String, offset : Int, limit : Int) : Flow<PagingData<TrackItem>>

    suspend fun getTrackById(id : String) : List<TrackEntity>
    suspend fun insertTrack(track : List<TrackEntity>)
    suspend fun deleteTracksById(id : String)

}