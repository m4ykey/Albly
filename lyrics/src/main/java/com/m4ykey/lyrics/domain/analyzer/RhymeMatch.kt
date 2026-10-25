package com.m4ykey.lyrics.domain.analyzer

data class RhymeMatch(
    val lineIndex : Int,
    val word : String,
    val groupId : Int,
    val type : RhymeType,
    val similarity : Double
)
