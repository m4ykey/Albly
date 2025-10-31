package com.m4ykey.albly.auth.token

suspend fun getToken(tokenProvider : SpotiTokenProvider) : String {
    return tokenProvider.getAccessToken().orEmpty()
}