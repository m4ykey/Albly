package com.m4ykey.network

import com.m4ykey.album.data.network.service.NewReleaseAlbumService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class NewReleaseServiceTest {

    @Test
    fun `getNewRelease should send correct request`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "/search",
                request.url.encodedPath
            )

            assertEquals(
                "2026",
                request.url.parameters["year"]
            )

            assertEquals(
                "2026-08-19",
                request.url.parameters["release_date"]
            )

            assertEquals(
                "year",
                request.url.parameters["sort"]
            )

            assertEquals(
                "desc",
                request.url.parameters["sort_order"]
            )

            assertEquals(
                "album",
                request.url.parameters["format"]
            )

            assertEquals(
                "20",
                request.url.parameters["per_page"]
            )

            assertEquals(
                "1",
                request.url.parameters["page"]
            )

            assertEquals(
                "master",
                request.url.parameters["type"]
            )

            respond(
                content = """
                    {
                        "results": []
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    "Content-Type",
                    "application/json"
                )
            )
        }

        val client = HttpClientFactory.create(
            engine = engine,
            enableLogging = false
        )

        val service = NewReleaseAlbumService(client)

        val result = service.getNewReleases(
            perPage = 20,
            page = 1,
            year = 2026,
            releaseDate = "2026-08-19"
        )

        assertEquals(0, result.results!!.size)
    }

}