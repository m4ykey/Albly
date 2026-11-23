@file:OptIn(ExperimentalPagingApi::class)

package com.m4ykey.album.data.remote_mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.m4ykey.album.data.mapper.toEntity
import com.m4ykey.album.data.mapper.toNewReleaseRoot
import com.m4ykey.album.data.network.service.RemoteNewReleaseAlbumService
import com.m4ykey.core.database.dao.NewReleaseDao
import com.m4ykey.core.database.model.album.NewReleaseCursor
import com.m4ykey.core.database.model.album.NewReleaseEntity
import com.m4ykey.core.network.safeApi
import kotlinx.coroutines.CancellationException

class NewReleaseRemoteMediator(
    private val dao : NewReleaseDao,
    private val service : RemoteNewReleaseAlbumService,
    private val year : Int,
    private val perPage : Int = 20
) : RemoteMediator<Int, NewReleaseEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cursor = dao.cursor()
        val fresh = cursor != null &&
                dao.countItems() > 0 &&
                System.currentTimeMillis() - cursor.fetchedAt < MS

        return if (fresh) InitializeAction.SKIP_INITIAL_REFRESH
        else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewReleaseEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> dao.cursor()?.nextPage
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val result = safeApi {
                service.getNewReleases(page = page, perPage = perPage, year = year)
            }.getOrThrow()

            val raw = result.toNewReleaseRoot().results
            val endOfPagination = raw.size < perPage

            val now = System.currentTimeMillis()
            val offset = (page - 1) * perPage

            val items = raw
                .filter { item -> item.masterId > 0 && item.title.isNotBlank() }
                .mapIndexed { index, item ->
                    item.toEntity(
                        year = year,
                        fetchedAt = now,
                        position = offset + index
                    )
                }

            val cursor = NewReleaseCursor(
                fetchedAt = now,
                nextPage = if (endOfPagination) null else page + 1
            )

            if (loadType == LoadType.REFRESH) {
                dao.replaceItems(items = items, cursor = cursor)
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
        const val MS = 6 * 60 * 60 * 1000L
    }
}