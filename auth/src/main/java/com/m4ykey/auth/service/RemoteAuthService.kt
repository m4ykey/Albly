package com.m4ykey.auth.service

import com.m4ykey.auth.model.Auth

interface RemoteAuthService {

    suspend fun getAccessToken(
        token : String,
        grantType : String
    ) : Auth

}