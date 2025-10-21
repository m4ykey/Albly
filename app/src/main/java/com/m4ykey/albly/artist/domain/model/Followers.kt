package com.m4ykey.albly.artist.domain.model

data class Followers(
    val total: Int
) {
    companion object {
        val EMPTY = Followers(0)
    }
}
