package com.m4ykey.artist.domain.model

data class Artist(
    val name : String,
    val id : Int,
    val uri : String,
    val images : List<Image>,
    val realname : String,
    val profile : String,
    val urls : List<String>
)
