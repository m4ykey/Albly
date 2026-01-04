package com.m4ykey.track.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.core.paging.pagingConfig
import com.m4ykey.track.data.local.dao.TrackDao
import com.m4ykey.track.data.local.model.TrackEntity
import com.m4ykey.track.data.network.service.RemoteTrackService
import com.m4ykey.track.data.paging.TrackPagingSource
import com.m4ykey.track.domain.model.TrackItem
import com.m4ykey.track.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class TrackRepositoryImpl(
    private val service : RemoteTrackService,
    private val dao : TrackDao
) : TrackRepository {

    override fun getAlbumTracks(
        id: String,
        offset: Int,
        limit: Int
    ): Flow<PagingData<TrackItem>> {
        return Pager(
            pagingSourceFactory = {
                TrackPagingSource(
                    id = id,
                    service = service
                )
            },
            config = pagingConfig
        ).flow
    }

    override fun getTrackById(id: String): Flow<PagingData<TrackEntity>> {
         return Pager(
             config = pagingConfig,
             pagingSourceFactory = { dao.getTrackById(id) }
         ).flow
    }

    override suspend fun insertTrack(track: List<TrackEntity>) {
        dao.insertTrack(track)
    }

    override suspend fun deleteTracksById(id: String) {
        dao.deleteTracksById(id)
    }

}