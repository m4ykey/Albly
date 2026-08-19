package com.m4ykey.network

import com.m4ykey.search.data.network.service.SearchLyricsService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class SearchGeniusLyricsServiceTest {

    @Test
    fun `searchLyrics should send correct request`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "/search",
                request.url.encodedPath
            )

            assertEquals(
                "Tyga",
                request.url.parameters["q"]
            )

            respond(
                content = """
                    {
                        "response": {}
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

        val service = SearchLyricsService(client)

        service.searchLyrics(query = "Tyga")
    }

}