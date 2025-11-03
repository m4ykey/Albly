package com.m4ykey.albly.auth.token

interface TokenHeaderProvider {
    fun getAuthorizationToken() : String?
}