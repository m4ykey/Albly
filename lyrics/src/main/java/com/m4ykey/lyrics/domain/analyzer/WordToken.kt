package com.m4ykey.lyrics.domain.analyzer

data class WordToken(
    val lineIndex : Int,
    val word : String,
    val normalized : String,
    val rhymeEnding : String,
    val wordStart : Int,
    val wordEnd : Int
)
