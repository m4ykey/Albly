package com.m4ykey.album.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m4ykey.album.R
import com.m4ykey.core.ui.AnimatedToggleIcon

@Composable
fun SaveButtonRow(
    modifier: Modifier = Modifier,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    onSaveClick : () -> Unit,
    onListenLaterClick : () -> Unit,
    divider : Boolean = false
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedToggleIcon(
            checked = isSaved,
            checkedIcon = Icons.Default.Favorite,
            unCheckedIcon = Icons.Outlined.FavoriteBorder,
            onClick = onSaveClick,
            contentDescription = stringResource(R.string.save),
            checkedColor = Color(0xFFE53935)
        )
        if (divider) {
            Spacer(modifier = Modifier.width(10.dp))
        }
        AnimatedToggleIcon(
            checked = isListenLaterSaved,
            checkedIcon = Icons.Default.AccessTimeFilled,
            unCheckedIcon = Icons.Default.AccessTime,
            onClick = onListenLaterClick,
            contentDescription = stringResource(R.string.listen_later),
            checkedColor = MaterialTheme.colorScheme.primary,
            wiggle = true
        )
    }
}