package com.m4ykey.search.presentation

import com.m4ykey.core.database.model.search.SearchItem

sealed interface DeleteSearchTarget {
    data class Single(val item : SearchItem) : DeleteSearchTarget
    data object All : DeleteSearchTarget
}