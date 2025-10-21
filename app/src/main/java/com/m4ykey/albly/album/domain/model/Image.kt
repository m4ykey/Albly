package com.m4ykey.albly.album.domain.model

data class Image(
    val height : Int,
    val width : Int,
    val url : String
) {
    companion object {
        val EMPTY_LIST = emptyList<Image>()
    }
}
