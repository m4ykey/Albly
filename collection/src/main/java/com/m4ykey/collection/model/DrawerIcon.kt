package com.m4ykey.collection.model

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun DrawerIcon(
    modifier: Modifier = Modifier,
    iconSource: IconSource,
    tint : Color = LocalContentColor.current,
    contentDescription : String?
) {
    when (iconSource) {
        is IconSource.Vector -> {
            Icon(
                tint = tint,
                imageVector = iconSource.imageVector,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
        is IconSource.Resource -> {
            Icon(
                tint = tint,
                painter = painterResource(iconSource.resId),
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
    }
}