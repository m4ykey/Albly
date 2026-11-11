package com.m4ykey.collection.presentation

import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.collection.presentation.type.ListViewType
import com.m4ykey.core.Sort

sealed interface CollectionAction {
    data class OnLinkClick(val url : String) : CollectionAction

    data class OnQueryChange(val query : String) : CollectionAction
    data object OnClearQuery : CollectionAction

    data object OnShowSearch : CollectionAction
    data object OnHideSearch : CollectionAction

    data object OnShowSortDialog : CollectionAction
    data object OnHideSortDialog : CollectionAction

    data object OnShowLinkDialog : CollectionAction
    data object OnHideLinkDialog : CollectionAction

    data class OnChangeList(val list : ListType) : CollectionAction
    data class OnChangeView(val view : ListViewType) : CollectionAction
    data class OnChangeSort(val sort : Sort) : CollectionAction
}