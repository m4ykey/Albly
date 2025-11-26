package com.m4ykey.auth.di

import com.m4ykey.auth.token.TokenProvider
import com.m4ykey.auth.token.configureSpotifyAuth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val spotifyApiModule = module {

    single(named("SpotifyHttpClient")) {
        HttpClient(get()) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorSpotifyAuth: $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(get())
            }

            configureSpotifyAuth(get<TokenProvider>())

            defaultRequest {
                url("https://api.spotify.com/v1/")
                url.protocol = URLProtocol.HTTPS
            }
        }
    }

}