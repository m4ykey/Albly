@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.lyrics.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.core.ui.ErrorCard
import com.m4ykey.core.ui.LoadImage
import com.m4ykey.lyrics.R
import com.m4ykey.lyrics.domain.model.LyricsItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LyricsScreen(
    artistName : String,
    trackName : String,
    albumName : String?,
    onBack : () -> Unit,
    imageUrl : String,
    viewModel: LyricsViewModel = koinViewModel()
) {

    val state = rememberLazyListState()

    val lyricsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(trackName, artistName, albumName) {
        viewModel.fetchLyrics(track = trackName, artist = artistName, album = albumName)
    }

    AppScaffold(
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = R.string.back
            )
        },
        content = { padding ->
            LyricsDisplay(
                state = state,
                imageUrl = imageUrl,
                paddingValues = padding,
                lyricsUiState = lyricsUiState
            )
        }
    )
}

@Composable
fun LyricsDisplay(
    modifier: Modifier = Modifier,
    lyricsUiState: LyricsUiState,
    paddingValues: PaddingValues,
    state: LazyListState,
    imageUrl: String
) {
    when (lyricsUiState) {
        is LyricsUiState.Loading -> {
            Box(modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator()
            }
        }
        is LyricsUiState.Error -> {
            ErrorCard(text = lyricsUiState.message)
        }
        is LyricsUiState.Success -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LyricsContent(
                    state = state,
                    contentPadding = paddingValues,
                    item = lyricsUiState.item,
                    imageUrl = imageUrl
                )
            }
        }
    }
}

@Composable
fun LyricsContent(
    modifier: Modifier = Modifier,
    state : LazyListState,
    contentPadding : PaddingValues = PaddingValues(0.dp),
    item : LyricsItem,
    imageUrl : String
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        state = state
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LoadImage(
                    modifier = Modifier.size(260.dp),
                    imageUrl = imageUrl
                )
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.trackName,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontSize = 20.sp
                )
                Text(
                    fontSize = 16.sp,
                    color = textColor2,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "${item.artistName} • ${item.albumName}"
                )
            }
        }
        item {
            Text(
                fontSize = 18.sp,
                color = textColor,
                text = item.plainLyrics
            )
        }
    }
}