package com.m4ykey.auth

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.m4ykey.auth.service.AuthService
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthServiceIntegrationTest {

    private fun generateBasicToken(clientId : String, clientSecret : String) : String {
        val credentials = "$clientId:$clientSecret"
        val base64Credentials = android.util.Base64.encodeToString(credentials.toByteArray(), android.util.Base64.NO_WRAP)
        return "Basic $base64Credentials"
    }

    private fun createAuthClient() : HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }

            defaultRequest {
                url("https://accounts.spotify.com/")
                url.protocol = URLProtocol.HTTPS
            }
        }
    }

    @Test
    fun getAccessToken_shouldReturnValidTokenFromSpotify() = runBlocking {
        val httpClient = createAuthClient()
        val authService = AuthService(httpClient)

        val clientId = com.m4ykey.auth.test.BuildConfig.SPOTIFY_CLIENT_ID
        val clientSecret = com.m4ykey.auth.test.BuildConfig.SPOTIFY_CLIENT_SECRET

        val basicToken = generateBasicToken(
            clientId,
            clientSecret,
        )

        val authResult = authService.getAccessToken(
            token = basicToken,
            grantType = "client_credentials"
        )

        assertTrue("Api returns error: ${authResult.errorDescription}", authResult.error.isNullOrBlank())

        val accessToken = authResult.accessToken
        assertNotNull("AccessToken should not be null", accessToken)
        assertFalse("AccessToken should not be empty", accessToken.isNullOrBlank())

        assertTrue("ExpiresIn should be greater than 0", (authResult.expiresIn ?: 0) > 0)
        println("Success! Token: ${accessToken!!.substring(0, 10)}...")
    }

}