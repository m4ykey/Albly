package com.m4ykey.collection.presentation

sealed interface CollectionTypeAction {
    data class OnLinkClick(val link : String) : CollectionTypeAction
    data class OnQueryChange(val text : String) : CollectionTypeAction
}