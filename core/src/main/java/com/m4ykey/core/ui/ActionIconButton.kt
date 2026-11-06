package com.m4ykey.core.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun ActionIconButton(
    modifier : Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconRes: Int,
    @StringRes textRes: Int,
    tint : Color = LocalContentColor.current
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            contentDescription = stringResource(textRes),
            painter = painterResource(iconRes),
            tint = tint
        )
    }
}

@Composable
fun ActionIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon : ImageVector,
    @StringRes textRes: Int,
    tint : Color = LocalContentColor.current
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            contentDescription = stringResource(textRes),
            imageVector = icon,
            tint = tint
        )
    }
}