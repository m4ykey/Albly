package com.m4ykey.albly.auth.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Auth(
    @param:Json(name = "access_token") val accessToken : String?,
    @param:Json(name = "token_type") val tokenType : String?,
    @param:Json(name = "expires_in") val expiresIn : Int?
)
