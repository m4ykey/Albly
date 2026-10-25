package com.m4ykey.lyrics.domain.analyzer

data class LineEnding(
    val lineIndex : Int,
    val originalWord : String,
    val normalizedWord : String,
    val rhymeEnding : String
)
