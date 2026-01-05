package com.m4ykey.collection.presentation.type.list

sealed interface ListTypeAction {
    data class OnChangeList(val list : ListType) : ListTypeAction
    data class OnChangeView(val view : ListViewType) : ListTypeAction
    data class OnChangeSort(val sort : ListSortType) : ListTypeAction
}