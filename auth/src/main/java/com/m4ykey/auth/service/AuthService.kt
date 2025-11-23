package com.m4ykey.auth.service

import com.m4ykey.auth.model.Auth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.parameters

class AuthService(
    private val client : HttpClient
) : RemoteAuthService {

    override suspend fun getAccessToken(
        token: String,
        grantType: String
    ): Auth {
        return client.submitForm(
            url = "api/token",
            formParameters = parameters {
                append("grant_type", grantType)
            }
        ) {
            header("Authorization", token)
        }.body()
    }
}