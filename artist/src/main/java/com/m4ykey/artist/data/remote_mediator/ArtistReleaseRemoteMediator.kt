@file:OptIn(ExperimentalPagingApi::class)

package com.m4ykey.artist.data.remote_mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.m4ykey.artist.data.mapper.toEntity
import com.m4ykey.artist.data.network.service.RemoteArtistService
import com.m4ykey.core.database.dao.ArtistReleaseDao
import com.m4ykey.core.database.model.artist.ArtistReleaseCursor
import com.m4ykey.core.database.model.artist.ArtistReleaseEntity
import com.m4ykey.core.network.safeApi
import kotlinx.coroutines.CancellationException

class ArtistReleaseRemoteMediator(
    private val dao : ArtistReleaseDao,
    private val service : RemoteArtistService,
    private val artistId : Int,
    private val perPage : Int = 20
) : RemoteMediator<Int, ArtistReleaseEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cursor = dao.cursor(artistId)
        val fresh = cursor != null &&
                dao.countItems(artistId) > 0 &&
                System.currentTimeMillis() - cursor.fetchedAt < MS

        return if (fresh) InitializeAction.SKIP_INITIAL_REFRESH
        else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArtistReleaseEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> dao.cursor(artistId)?.nextPage
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val result = safeApi {
                service.getArtistReleases(id = artistId, page = page, perPage = perPage)
            }.getOrThrow()

            val raw = result.releases
            val endOfPagination = result.pagination?.page?.let { pages -> page >= pages }
                ?: (raw.size < perPage)

            val now = System.currentTimeMillis()
            val offset = (page - 1) * perPage

            val items = raw
                .filter { item -> (item.id ?: 0) > 0 && !item.title.isNullOrBlank() }
                .mapIndexed { index, item ->
                    item.toEntity(
                        artistId = artistId,
                        fetchedAt = now,
                        position = offset + index
                    )
                }

            val cursor = ArtistReleaseCursor(
                artistId = artistId,
                fetchedAt = now,
                nextPage = if (endOfPagination) null else page + 1
            )

            if (loadType == LoadType.REFRESH) {
                dao.replaceItems(artistId = artistId, items = items, cursor = cursor)
            } else {
                dao.insertItems(items)
                dao.upsertCursor(cursor)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e : CancellationException) {
            throw e
        } catch (e : Exception) {
            MediatorResult.Error(e)
        }
    }

    private companion object {
        const val MS = 24 * 60 * 60 * 1000L
    }
}