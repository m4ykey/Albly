package com.m4ykey.collection.presentation.type

import com.m4ykey.album.data.local.model.AlbumSort

data class ListTypeState(
    val view : ListViewType,
    val type : ListType,
    val sort : AlbumSort
)