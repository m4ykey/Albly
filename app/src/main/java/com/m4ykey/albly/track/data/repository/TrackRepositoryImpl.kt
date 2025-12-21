package com.m4ykey.albly.track.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.albly.track.data.local.dto.TrackDao
import com.m4ykey.albly.track.data.local.model.TrackEntity
import com.m4ykey.albly.track.data.network.service.RemoteTrackService
import com.m4ykey.albly.track.data.paging.TrackPagingSource
import com.m4ykey.albly.track.domain.model.TrackItem
import com.m4ykey.albly.track.domain.repository.TrackRepository
import com.m4ykey.core.paging.pagingConfig
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

    override fun getTrackById(id: String): List<TrackEntity> {
         return dao.getTrackById(id)
    }

    override suspend fun insertTrack(track: List<TrackEntity>) {
        dao.insertTrack(track)
    }

    override suspend fun deleteTracksById(id: String) {
        dao.deleteTracksById(id)
    }

}