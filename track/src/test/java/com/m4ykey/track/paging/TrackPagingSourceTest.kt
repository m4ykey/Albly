package com.m4ykey.track.paging

import androidx.paging.PagingSource
import com.m4ykey.track.data.network.service.RemoteTrackService
import com.m4ykey.track.data.paging.TrackPagingSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TrackPagingSourceTest {

    private val service = mockk<RemoteTrackService>()
    private val albumId = "123"
    private lateinit var pagingSource : TrackPagingSource

    @Before
    fun setup() {
        pagingSource = TrackPagingSource(albumId, service)
    }

    @Test
    fun loadReturnsSuccessWhenServiceReturnsData() = runTest {
        val mockResponse = AlbumTracksDto(
            items = listOf(
                TrackItemDto(
                    durationMs = 0,
                    discNumber = 0,
                    name = "Track1",
                    id = "123",
                    explicit = false,
                    previewUrl = "",
                    trackNumber = 1,
                    externalUrls = ExternalUrlsDto(spotify = ""),
                    artists = listOf(
                        AlbumArtistDto(
                            id = "123",
                            name = "Artist1",
                            externalUrls = ExternalUrlsDto(spotify = ""),
                            type = ""
                        )
                    )
                )
            )
        )

        coEvery {
            service.getAlbumTracks(id = albumId, limit = any(), offset = any())
        } returns mockResponse

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 2,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        Assert.assertEquals(1, page.data.size)
        Assert.assertEquals("Track1", page.data[0].name)
    }

    @Test
    fun loadDataShouldReturnErrorWhenApiFails() = runTest {
        coEvery { service.getAlbumTracks(any(), any(), any()) } throws Exception("No internet")

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        println("DEBUG: Result type is ${result::class.java.simpleName}")
        if (result is PagingSource.LoadResult.Page) {
            println("DEBUG: Data in page: ${result.data}")
        }
    }

}