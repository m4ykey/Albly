package com.m4ykey.core.model.domain

data class ExternalUrls(
    val spotify : String
) {
    companion object {
        val EMPTY = ExternalUrls("")
    }
}