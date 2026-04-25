package com.m4ykey.album.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.album.data.local.dao.AlbumDao
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.local.model.AlbumWithStates
import com.m4ykey.album.data.local.model.IsAlbumSaved
import com.m4ykey.album.data.local.model.IsListenLaterSaved
import com.m4ykey.album.data.network.paging.NewReleasePagingSource
import com.m4ykey.album.data.network.service.RemoteAlbumService
import com.m4ykey.album.data.network.service.RemoteNewReleaseAlbumService
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.domain.repository.AlbumRepository
import com.m4ykey.album.mapper.AlbumMapper
import com.m4ykey.core.paging.pagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class AlbumRepositoryImpl(
    private val service : RemoteAlbumService,
    private val dao : AlbumDao,
    private val newReleaseService : RemoteNewReleaseAlbumService
) : AlbumRepository {

    override suspend fun getAlbum(id: Int): Flow<AlbumRoot> = flow {
        val response = service.getAlbum(id)

        val domainAlbum =  AlbumMapper.mapToDomain(response)
            ?: throw Exception("")

        emit(domainAlbum)
    }

    override fun getNewRelease(): Flow<PagingData<NewReleaseResult>> {
        val today = LocalDate.now()
        val currentFriday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY))
        val lastFriday = currentFriday.minusWeeks(1)

        val dateRange = "$lastFriday..$currentFriday"

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                NewReleasePagingSource(
                    service = newReleaseService,
                    releaseDate = dateRange
                )
            }
        ).flow
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

    override fun getLocalAlbumById(id: Int): AlbumEntity? {
        return dao.getAlbumById(id)
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

    override fun getSavedAlbums(query : String): Flow<List<AlbumEntity>> {
        return dao.getSavedAlbums(query)
    }

    override suspend fun getListenLaterAlbums(): List<AlbumEntity> {
        return dao.getListenLaterAlbums()
    }

    override suspend fun getRandomAlbum(): Flow<AlbumEntity> {
        return dao.getRandomAlbum()
    }
}