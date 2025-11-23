package com.m4ykey.auth.token

interface TokenProvider {
    suspend fun getAccessToken() : String?
    suspend fun fetchAndSaveNewToken() : String
}