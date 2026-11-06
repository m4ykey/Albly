package com.m4ykey.collection.presentation.type

import com.m4ykey.core.Sort

data class ListTypeState(
    val view : ListViewType,
    val type : ListType,
    val sort : Sort
)