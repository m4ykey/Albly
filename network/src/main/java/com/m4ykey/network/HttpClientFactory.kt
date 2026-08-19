package com.m4ykey.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        engine : HttpClientEngine = CIO.create(),
        enableLogging : Boolean = false,
        token: String? = null,
        baseUrl : String? = null,
        isTokenInUrl : Boolean = false
    ) : HttpClient = HttpClient(engine) {

        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true })
        }

        if (enableLogging) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO

                sanitizeHeader { header ->
                    header == HttpHeaders.Authorization
                }
            }
        }

        defaultRequest {
            baseUrl?.let { url(it) }
            token?.let {
                if (isTokenInUrl) {
                    url.parameters.append("token", it)
                } else {
                    header("Authorization", "Bearer $it")
                }
            }
        }
    }
}