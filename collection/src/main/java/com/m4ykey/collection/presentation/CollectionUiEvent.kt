package com.m4ykey.collection.presentation

sealed interface CollectionUiEvent {
    data class OnLinkClick(val link : String) : CollectionUiEvent
}