package com.m4ykey.albly.collection.presentation.type.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.m4ykey.albly.R

enum class ListSortType {
    LATEST,
    OLDEST,
    ALPHABETICAL
}

@Composable
fun ListSortType.label() : String = when (this) {
    ListSortType.ALPHABETICAL -> stringResource(R.string.alphabetical)
    ListSortType.LATEST -> stringResource(R.string.latest)
    ListSortType.OLDEST -> stringResource(R.string.oldest)
}