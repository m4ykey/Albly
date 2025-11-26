package com.m4ykey.auth.token

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer

fun HttpClientConfig<*>.configureSpotifyAuth(tokenProvider : TokenProvider) {
    install(Auth) {
        bearer {
            loadTokens {
                val cachedToken = tokenProvider.getAccessToken()

                if (!cachedToken.isNullOrBlank()) {
                    return@loadTokens BearerTokens(accessToken = cachedToken, refreshToken = "")
                }

                val newAccessToken = tokenProvider.fetchAndSaveNewToken()

                return@loadTokens if (newAccessToken.isNotBlank()) {
                    BearerTokens(accessToken = newAccessToken, refreshToken = "")
                } else {
                    null
                }
            }

            refreshTokens {
                val newToken = tokenProvider.fetchAndSaveNewToken()

                return@refreshTokens if (newToken.isNotBlank()) {
                    BearerTokens(accessToken = newToken, refreshToken = "")
                } else {
                    null
                }
            }
        }
    }
}