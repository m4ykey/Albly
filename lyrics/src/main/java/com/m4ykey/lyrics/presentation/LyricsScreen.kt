@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.lyrics.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.core.ui.ErrorCard
import com.m4ykey.core.ui.LoadImage
import com.m4ykey.lyrics.R
import com.m4ykey.lyrics.domain.analyzer.RhymeDetector
import com.m4ykey.lyrics.domain.analyzer.RhymeGroup
import com.m4ykey.lyrics.domain.analyzer.RhymeMatch
import com.m4ykey.lyrics.domain.model.LyricsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.copy

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

private val RhymeColorsDark = listOf(
    Color(0xFF80CBC4),
    Color(0xFFFFCC80),
    Color(0xFFCE93D8),
    Color(0xFF90CAF9),
    Color(0xFFF48FB1),
    Color(0xFFA5D6A7)
)

private val RhymeColorsLight = listOf(
    Color(0xFF00796B),
    Color(0xFFB45309),
    Color(0xFF7B1FA2),
    Color(0xFF1565C0),
    Color(0xFFC2185B),
    Color(0xFF2E7D32)
)

@Composable
fun LyricsContent(
    modifier: Modifier = Modifier,
    state : LazyListState,
    contentPadding : PaddingValues = PaddingValues(0.dp),
    item : LyricsItem,
    imageUrl : String
) {
    val isDarkTheme = isSystemInDarkTheme()

    val textColor = if (isDarkTheme) Color.White else Color.Black
    val textColor2 = if (isDarkTheme) Color(0xFFBDBDBD) else Color(0xFF424242)

    var showRhymes by remember(item.id) {
        mutableStateOf(false)
    }

    val lines = remember(item.plainLyrics) { item.plainLyrics.lines() }

    val rhymeGroups by produceState(
        key1 = item.id,
        key2 = item.plainLyrics,
        initialValue = emptyList()
    ) {
        value = emptyList()
        value = withContext(Dispatchers.Default) {
            RhymeDetector.analyze(item.plainLyrics)
        }
    }

    val matchesByLine = remember(rhymeGroups) {
        rhymeGroups
            .flatMap { it.matches }
            .groupBy { it.lineIndex }
            .mapValues { (_, list) -> list.sortedBy { it.wordStart } }
    }

    val rhymeColors = if (isDarkTheme) RhymeColorsDark else RhymeColorsLight

    val plainText = remember(lines) {
        AnnotatedString(lines.joinToString("\n"))
    }

    val rhymedText = remember(lines, matchesByLine, rhymeColors) {
        buildRhymedText(lines, matchesByLine, rhymeColors)
    }

    val highlightedLyrics = if (showRhymes) rhymedText else plainText

    LazyColumn(
        modifier = modifier
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(R.string.rhyme_highlighting),
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = if (showRhymes) {
                            "${rhymeGroups.size} ${stringResource(R.string.rhyme_groups)}"
                        } else {
                            stringResource(R.string.disabled)
                        },
                        color = textColor2,
                        fontSize = 12.sp
                    )
                }

                Switch(
                    checked = showRhymes,
                    onCheckedChange = { showRhymes = it }
                )
            }
        }

        item {
            Text(
                fontSize = 18.sp,
                color = textColor,
                text = highlightedLyrics,
                lineHeight = 28.sp
            )
        }
    }
}

private fun buildRhymedText(
    lines: List<String>,
    matchesByLine: Map<Int, List<RhymeMatch>>,
    colors: List<Color>
): AnnotatedString = buildAnnotatedString {
    lines.forEachIndexed { index, line ->
        val lineMatches = matchesByLine[index]

        if (lineMatches.isNullOrEmpty()) {
            append(line)
        } else {
            var cursor = 0
            for (match in lineMatches) {
                append(line.substring(cursor, match.wordStart))

                val color = colors[match.groupId % colors.size]
                withStyle(
                    SpanStyle(
                        color = color,
                        background = color.copy(alpha = 0.16f),
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append(line.substring(match.wordStart, match.wordEnd))
                }
                cursor = match.wordEnd
            }
            append(line.substring(cursor))
        }

        if (index < lines.lastIndex) append('\n')
    }
}