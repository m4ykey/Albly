package com.m4ykey.albly.search.presentation

sealed interface SearchTypeAction {
    data class OnTypeClick(val type : SearchType) : SearchTypeAction
}