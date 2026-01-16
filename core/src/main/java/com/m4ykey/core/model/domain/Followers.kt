package com.m4ykey.core.model.domain

data class Followers(
    val total: Int
) {
    companion object {
        val EMPTY = Followers(0)
    }
}
