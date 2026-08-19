package com.m4ykey.network

import com.m4ykey.album.data.network.service.AlbumService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class AlbumServiceTest {

    @Test
    fun `getAlbum should send correct request`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "/masters/123",
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

        val service = AlbumService(client)

        val result = service.getAlbum(123)

        assertEquals(123, result.id)
    }

}