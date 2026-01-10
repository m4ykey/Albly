package com.m4ykey.auth.token

import com.m4ykey.auth.service.RemoteAuthService
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
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
    } catch (e: ClientRequestException) {
        throw RuntimeException("Client error (4xx): ${e.response.status}", e)
    } catch (e: ServerResponseException) {
        throw RuntimeException("Server error (5xx): ${e.response.status}", e)
    } catch (e: ResponseException) {
        throw RuntimeException("HTTP error: ${e.response.status}", e)
    } catch (e: IOException) {
        throw RuntimeException("Network error occurred", e)
    } catch (e: Exception) {
        throw RuntimeException("Unexpected error occurred", e)
    }
}