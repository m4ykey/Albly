package com.m4ykey.albly.search.presentation

sealed interface SearchTypeAction {
    data class OnTypeClick(val type : SearchType) : SearchTypeAction
    data class OnQueryChange(val query : String) : SearchTypeAction
    data object OnSearchClick : SearchTypeAction
    data class OnAlbumClick(val id : String) : SearchTypeAction
}