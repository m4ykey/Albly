package com.m4ykey.network

import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import org.junit.Test

class HttpClientFactoryTest {

    @Test
    fun `should use base url`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "https://example.com/api/test",
                request.url.toString()
            )

            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        val client = HttpClientFactory.create(
            engine = engine,
            baseUrl = "https://example.com/api/"
        )

        client.get("test")
    }

    @Test
    fun `should add bearer token to authorization header`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "Bearer test-token",
                request.headers["Authorization"]
            )

            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        val client = HttpClientFactory.create(
            engine = engine,
            token = "test-token"
        )

        client.get("test")
    }

    @Test
    fun `should add token to url`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                "test-token",
                request.url.parameters["token"]
            )

            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        val client = HttpClientFactory.create(
            engine = engine,
            token = "test-token",
            isTokenInUrl = true
        )

        client.get("test")
    }

    @Test
    fun `should not add authorization when token is null`() = runTest {
        val engine = MockEngine { request ->
            assertEquals(
                null,
                request.headers["Authorization"]
            )

            assertEquals(
                null,
                request.url.parameters["token"]
            )

            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        val client = HttpClientFactory.create(
            engine = engine
        )

        client.get("test")
    }

    @Test
    fun `should ignore unknown json fields`() = runTest {
        val engine = MockEngine {
            respond(
                content = """
                    {
                        "id": 123,
                        "unknownField": "ignored"
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = io.ktor.http.headersOf(
                    "Content-Type",
                    "application/json"
                )
            )
        }

        val client = HttpClientFactory.create(
            engine = engine
        )

        val response = client.get("test").body<TestResponse>()

        assertEquals(123, response.id)
    }

    @Test
    fun `should create client with logging enabled`() = runTest {
        val engine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        val client = HttpClientFactory.create(
            engine = engine,
            enableLogging = true
        )

        val response = client.get("test")

        assertEquals(
            HttpStatusCode.OK,
            response.status
        )
    }

}