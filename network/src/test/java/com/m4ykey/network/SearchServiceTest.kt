package com.m4ykey.network

import com.m4ykey.search.data.network.service.SearchService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class SearchServiceTest {

    @Test
    fun `searchAlbum should send correct request`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "/search",
                request.url.encodedPath
            )

            assertEquals(
                "Tyga",
                request.url.parameters["q"]
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

        val service = SearchService(client)

        val result = service.searchAlbum(query = "Tyga", page = 1)

        assertEquals(0, result.results.size)
    }

    @Test
    fun `searchArtist should send correct request`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "/search",
                request.url.encodedPath
            )

            assertEquals(
                "Tyga",
                request.url.parameters["q"]
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
                "artist",
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

        val service = SearchService(client)

        val result = service.searchArtist(query = "Tyga", page = 1)

        assertEquals(0, result.results.size)
    }
}