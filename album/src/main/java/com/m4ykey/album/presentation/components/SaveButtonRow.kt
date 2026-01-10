package com.m4ykey.album.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m4ykey.album.R

@Composable
fun SaveButtonRow(
    modifier: Modifier = Modifier,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    onSaveClick : () -> Unit,
    onListenLaterClick : () -> Unit
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            contentDescription = stringResource(R.string.save),
            imageVector = if (isSaved) {
                Icons.Default.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            },
            modifier = modifier.clickable { onSaveClick() }
        )

        Icon(
            contentDescription = stringResource(R.string.listen_later),
            imageVector = if (isListenLaterSaved) {
                Icons.Default.AccessTimeFilled
            } else {
                Icons.Default.AccessTime
            },
            modifier = modifier.clickable { onListenLaterClick() }
        )
    }
}