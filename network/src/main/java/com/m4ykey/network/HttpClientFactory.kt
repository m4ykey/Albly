package com.m4ykey.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        enableLogging : Boolean = true,
        timeoutMs: Long = 15_000L
    ) : HttpClient = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = timeoutMs
            connectTimeoutMillis = timeoutMs
            socketTimeoutMillis = timeoutMs
        }

        if (enableLogging) {
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }
        }

        defaultRequest {
            headers.append("Accept", "application/json")
        }

        engine {
            maxConnectionsCount = 64
            endpoint {
                connectTimeout = timeoutMs
                socketTimeout = timeoutMs
                keepAliveTime = 5_000
                connectAttempts  = 3
            }
        }
    }
}