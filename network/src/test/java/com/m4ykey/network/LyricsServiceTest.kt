package com.m4ykey.network

import com.m4ykey.lyrics.data.network.service.LyricsService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class LyricsServiceTest {

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

            assertEquals(
                "Taste",
                request.url.parameters["track_name"]
            )

            respond(
                content = "[]",
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

        val service = LyricsService(client)

        val result = service.searchLyrics(q = "Tyga", trackName = "Taste")

        assertEquals(0, result.size)
    }

    @Test
    fun `getLyrics should send correct request`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "/get/123",
                request.url.encodedPath
            )

            assertEquals(
                HttpMethod.Get,
                request.method
            )

            respond(
                content = """
                    {
                        "id": 123
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

        val service = LyricsService(client)

        service.getLyrics(123)
    }

}