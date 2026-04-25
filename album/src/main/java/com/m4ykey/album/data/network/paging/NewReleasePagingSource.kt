package com.m4ykey.album.data.network.paging

import com.m4ykey.album.data.network.service.RemoteNewReleaseAlbumService
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.mapper.AlbumMapper
import com.m4ykey.core.network.safeApi
import com.m4ykey.core.paging.BasePagingSource

class NewReleasePagingSource(
    private val service : RemoteNewReleaseAlbumService,
    private val releaseDate : String
) : BasePagingSource<NewReleaseResult>() {
    override suspend fun loadData(
        page: Int,
        limit: Int
    ): Result<List<NewReleaseResult>> {
        return safeApi {
            service.getNewReleases(
                page = page,
                releaseDate = releaseDate
            )
        }.map { response ->
            AlbumMapper.mapToNewRelease(response).results
        }
    }


}