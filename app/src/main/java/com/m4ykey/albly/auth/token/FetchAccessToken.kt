package com.m4ykey.albly.auth.token

import com.m4ykey.albly.auth.network.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

suspend fun fetchAccessToken(
    api : AuthApi,
    clientId : String,
    clientSecret : String
) : String {
    return try {
        withContext(Dispatchers.IO) {
            val response = api.getAccessToken(
                token = generateToken(clientId, clientSecret),
                grantType = "client_credentials"
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