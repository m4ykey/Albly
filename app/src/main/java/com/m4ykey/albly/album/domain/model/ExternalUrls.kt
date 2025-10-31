package com.m4ykey.albly.album.domain.model

data class ExternalUrls(
    val spotify : String
) {
    companion object {
        val EMPTY = ExternalUrls("")
    }
}
