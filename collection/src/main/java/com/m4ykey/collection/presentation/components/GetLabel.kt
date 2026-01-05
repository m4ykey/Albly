package com.m4ykey.collection.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.m4ykey.collection.R
import com.m4ykey.core.model.type.AlbumType

@Composable
fun AlbumType.getLabel() : String {
    return when (this) {
        AlbumType.ALBUM -> stringResource(id = R.string.album)
        AlbumType.EP -> stringResource(id = R.string.ep)
        AlbumType.SINGLE -> stringResource(id = R.string.single)
        AlbumType.COMPILATION -> stringResource(id = R.string.compilation)
        AlbumType.EMPTY -> ""
    }
}