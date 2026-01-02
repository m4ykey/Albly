package com.m4ykey.track.service

import com.m4ykey.track.data.network.dto.AlbumTracksDto
import com.m4ykey.track.data.network.dto.TrackItemDto
import com.m4ykey.track.data.network.service.RemoteTrackService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TrackServiceTest {

    private val service = mockk<RemoteTrackService>()

    @Test
    fun getAlbumTracksShouldReturnExpectedDto() = runTest {
        val mockDto = AlbumTracksDto(
            items = listOf(
                TrackItemDto(id = "1", name = "Test Track")
            )
        )

        coEvery {
            service.getAlbumTracks(id = any(), limit = any(), offset = any())
        } returns mockDto

        val result = service.getAlbumTracks("some_id", 20, 0)

        Assert.assertEquals(1, result.items.size)
        Assert.assertEquals("Test Track", result.items[0].name)

        coVerify { service.getAlbumTracks("some_id", 20, 0) }
    }

}