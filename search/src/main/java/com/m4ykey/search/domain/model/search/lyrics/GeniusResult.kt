package com.m4ykey.search.domain.model.search.lyrics

import java.util.UUID

data class GeniusResult(
    val title : String,
    val artistNames : String,
    val songArtImageUrl : String,
    val id : UUID = UUID.randomUUID()
)
