package com.m4ykey.auth.token

import android.net.http.HttpException
import com.m4ykey.auth.service.RemoteAuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException

suspend fun fetchAccessToken(
    service : RemoteAuthService,
    clientId : String,
    clientSecret : String
) : String {
    return try {
        withContext(Dispatchers.IO) {
            val grantType = "client_credentials"
            val token = generateToken(clientId, clientSecret)

            val response = service.getAccessToken(
                token = token,
                grantType = grantType
            )
            response.accessToken ?: throw RuntimeException("Failed to fetch access token")
        }
    } catch (e : IOException) {
        throw RuntimeException("Network error occurred", e)
    } catch (e : HttpException) {
        throw RuntimeException("HTTP error occurred", e)
    } catch (e : Exception) {
        throw RuntimeException("Unexpected error occurred", e)
    }
}