package com.m4ykey.collection.presentation

import com.m4ykey.album.data.local.model.AlbumSort
import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListViewType

sealed interface CollectionTypeAction {
    data class OnLinkClick(val link : String) : CollectionTypeAction
    data class OnQueryChange(val text : String) : CollectionTypeAction

    data class OnChangeList(val list : ListType) : CollectionTypeAction
    data class OnChangeView(val view : ListViewType) : CollectionTypeAction
    data class OnChangeSort(val sort : AlbumSort) : CollectionTypeAction
}