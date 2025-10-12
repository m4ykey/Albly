package com.m4ykey.albly.util

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenteredContent(
    modifier : Modifier = Modifier,
    content : @Composable (contentModifier : Modifier) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val isWideScreen = maxWidth > 600.dp

        val contentModifier = Modifier.then(
            if (isWideScreen) {
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .widthIn(max = 800.dp)
                    .align(Alignment.TopCenter)
            } else {
                Modifier.fillMaxWidth()
            }
        )

        content(contentModifier)
    }
}