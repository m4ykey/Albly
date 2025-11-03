package com.m4ykey.albly.auth.token

interface TokenProvider {
    suspend fun getAccessToken() : String?
}