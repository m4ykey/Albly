package com.m4ykey.auth.di

import com.m4ykey.auth.service.AuthService
import com.m4ykey.auth.service.RemoteAuthService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

    single {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    single(named("AuthHttpClient")) {
        HttpClient(Android) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorAuth: $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(get())
            }

            defaultRequest {
                url("https://accounts.spotify.com/")
                url.protocol = URLProtocol.HTTPS
            }
        }
    }

    single<RemoteAuthService> {
        AuthService(get(named("AuthHttpClient")))
    }
}