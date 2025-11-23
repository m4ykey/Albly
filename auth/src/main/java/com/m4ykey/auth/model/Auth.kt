package com.m4ykey.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Auth(
    @SerialName("access_token") val accessToken : String? = null,
    @SerialName("token_type") val tokenType : String? = null,
    @SerialName("expires_in") val expiresIn : Int? = null,
    val error : String? = null,
    @SerialName("error_description") val errorDescription : String? = null
)
