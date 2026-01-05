package com.m4ykey.album.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            painter = if (isSaved) {
                painterResource(R.drawable.ic_favorite)
            } else {
                painterResource(R.drawable.ic_favorite_border)
            },
            modifier = modifier.clickable { onSaveClick() }
        )

        Icon(
            contentDescription = stringResource(R.string.listen_later),
            painter = if (isListenLaterSaved) {
                painterResource(R.drawable.ic_access_time)
            } else {
                painterResource(R.drawable.ic_access_time_outline)
            },
            modifier = modifier.clickable { onListenLaterClick() }
        )
    }
}