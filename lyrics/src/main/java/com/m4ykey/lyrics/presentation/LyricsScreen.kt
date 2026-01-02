@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.lyrics.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        content = {
            LyricsDisplay(
                state = state,
                title = trackName,
                artist = artistName
            )
        }
    )
}

@Composable
fun LyricsDisplay(
    modifier: Modifier = Modifier,
    state: LazyListState,
    title : String,
    artist : String
) {
    LyricsContent(
        state = state,
        title = title,
        artist = artist
    )
}

@Composable
fun LyricsContent(
    modifier: Modifier = Modifier,
    contentPadding : PaddingValues = PaddingValues(0.dp),
    state : LazyListState,
    title : String,
    artist : String
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

            }
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = modifier.fillMaxSize()) {
                    
                }
            }
        }
    }
}