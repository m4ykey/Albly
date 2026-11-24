@file:OptIn(ExperimentalPagingApi::class)

package com.m4ykey.artist.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.m4ykey.artist.data.mapper.toArtist
import com.m4ykey.artist.data.mapper.toReleaseItem
import com.m4ykey.artist.data.remote_mediator.ArtistReleaseRemoteMediator
import com.m4ykey.artist.data.network.service.RemoteArtistService
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.domain.model.ReleaseItem
import com.m4ykey.artist.domain.repository.ArtistRepository
import com.m4ykey.core.database.dao.ArtistDao
import com.m4ykey.core.database.dao.ArtistReleaseDao
import com.m4ykey.core.database.model.artist.ArtistEntity
import com.m4ykey.core.database.model.artist.ArtistListItem
import com.m4ykey.core.database.model.artist.ArtistWithStates
import com.m4ykey.core.database.model.artist.IsArtistSaved
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.pagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ArtistRepositoryImpl(
    private val service : RemoteArtistService,
    private val dao : ArtistReleaseDao,
    private val artistDao : ArtistDao
) : ArtistRepository {

    companion object {
        const val MAX_CACHED_ARTISTS = 200
    }

    override suspend fun getArtistById(id: Int): Flow<Artist> = flow {
        val result = safeApi {
            service.getArtistById(id)
        }.map { it.toArtist() }

        result.fold(
            onSuccess = { item -> emit(item) },
            onFailure = { error ->
                val cached = artistDao.getArtistWithStates(id)?.artistEntity
                    ?: throw error
                emit(cached.toArtist())
            }
        )
    }.flowOn(Dispatchers.IO)

    override fun getArtistReleases(id: Int): Flow<PagingData<ReleaseItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { dao.pagingSource(id) },
            remoteMediator = ArtistReleaseRemoteMediator(
                dao = dao,
                service = service,
                artistId = id,
                perPage = pagingConfig.pageSize
            )
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toReleaseItem() }
        }
    }

    override suspend fun insertArtist(artist: ArtistEntity) {
        artistDao.insertArtist(artist)
    }

    override suspend fun insertSavedArtist(isArtistSaved: IsArtistSaved) {
        artistDao.insertSavedArtist(isArtistSaved)
    }

    override suspend fun getSavedArtistState(id: Int): IsArtistSaved? {
        return artistDao.getSavedArtistState(id)
    }

    override suspend fun getArtistWithStates(id: Int): ArtistWithStates? {
        return artistDao.getArtistWithStates(id)
    }

    override suspend fun deleteArtist(id: Int) {
        artistDao.deleteArtist(id)
    }

    override suspend fun deleteSavedArtistState(id: Int) {
        artistDao.deleteSavedArtistState(id)
    }

    override fun getSavedArtistLatest(query: String): Flow<List<ArtistListItem>> {
        return artistDao.getSavedArtistLatest(query)
    }

    override fun getSavedArtistOldest(query: String): Flow<List<ArtistListItem>> {
        return artistDao.getSavedArtistOldest(query)
    }

    override fun getSavedArtistAlphabetical(query: String): Flow<List<ArtistListItem>> {
        return artistDao.getSavedArtistAlphabetical(query)
    }

    override suspend fun cacheArtist(artist: ArtistEntity) {
        if (artistDao.isArtistStored(artist.id)) return

        artistDao.insertArtist(artist)

        val cached = artistDao.countCachedArtists()
        if (cached > MAX_CACHED_ARTISTS) {
            artistDao.deleteOldestCachedArtists(cached - MAX_CACHED_ARTISTS)
        }
    }
}