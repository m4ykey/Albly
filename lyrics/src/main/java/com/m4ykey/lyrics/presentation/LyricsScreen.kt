@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.lyrics.presentation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.lyrics.R

@Composable
fun LyricsScreen(
    artistName : String,
    trackName : String,
    onBack : () -> Unit
) {

    val state = rememberLazyListState()

    AppScaffold(
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = R.string.back
            )
        },
        content = { padding ->

        }
    )
}