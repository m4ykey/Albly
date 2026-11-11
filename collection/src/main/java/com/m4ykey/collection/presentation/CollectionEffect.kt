package com.m4ykey.collection.presentation

sealed interface CollectionEffect {
    data class ShowError(val message : String) : CollectionEffect
    data class OpenLink(val albumId : String) : CollectionEffect
    data object InvalidLink : CollectionEffect
}