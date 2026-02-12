@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)

package com.m4ykey.search.presentation

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.m4ykey.core.chip.ChipItem
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.CenteredContent
import com.m4ykey.core.paging.BasePagingList
import com.m4ykey.core.paging.ErrorItem
import com.m4ykey.core.ui.AlbumCard
import com.m4ykey.core.ui.ArtistCard
import com.m4ykey.search.R
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
    onAlbumClick : (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isDarkTheme = if (isSystemInDarkTheme()) Color.White else Color.Black
    val albumSearchItems = viewModel.searchAlbumResults.collectAsLazyPagingItems()
    val artistSearchItems = viewModel.searchArtistResults.collectAsLazyPagingItems()

    val targetWeight = if (searchQuery.isEmpty()) 1f else 0.8f

    val animatedWeight by animateFloatAsState(
        label = "SearchBarWeightAnimation",
        targetValue = targetWeight,
        animationSpec = tween(durationMillis = 1000)
    )

    val type by viewModel.searchType.map { it.type }.collectAsStateWithLifecycle(initialValue = SearchType.ALBUM)
    val activeSearchQuery by viewModel.activeSearchQuery.collectAsStateWithLifecycle()

    val onAction = viewModel::onAction

    val state = rememberLazyGridState()

    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.searchUiEvent.collect { event ->
            when (event) {
                is SearchUiEvent.ChangeType -> viewModel.updateType(event.type)
                is SearchUiEvent.OnAlbumClick -> onAlbumClick(event.id)
            }
        }
    }

    LaunchedEffect(type) {
        state.scrollToItem(0)
    }

    val permissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = matches?.getOrNull(0) ?: ""

            if (!matches.isNullOrEmpty()) {
                onAction(SearchTypeAction.OnQueryChange(spokenText))
            }
        }
    }

    val speechPrompt = stringResource(R.string.enter_album_name)

    val errorMessage = stringResource(R.string.speech_recognition_not_supported)

    val startSpeechRecognition = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, speechPrompt)
        }
        try {
            launcher.launch(intent)
        } catch (e : Exception) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    val handleMick = {
        when {
            permissionState.status.isGranted -> {
                startSpeechRecognition()
            }
            else -> {
                permissionState.launchPermissionRequest()
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionIconButton(
                    tint = isDarkTheme,
                    onClick = onBack,
                    textRes = com.m4ykey.core.R.string.back,
                    iconRes = com.m4ykey.core.R.drawable.ic_arrow_left
                )
                SearchBarTextField(
                    searchQuery = searchQuery,
                    onValueChange = { query ->
                        onAction(SearchTypeAction.OnQueryChange(query))
                    },
                    modifier = Modifier
                        .weight(animatedWeight)
                        .padding(start = 8.dp),
                    onSearch = { onAction(SearchTypeAction.OnSearchClick) }
                )
                ActionIconButton(
                    tint = isDarkTheme,
                    onClick = { handleMick() },
                    textRes = R.string.mic,
                    iconRes = R.drawable.ic_mic
                )
                AnimatedVisibility(
                    visible = searchQuery.isNotEmpty(),
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth }
                    )
                ) {
                    ActionIconButton(
                        onClick = { onAction(SearchTypeAction.OnQueryChange("")) },
                        tint = isDarkTheme,
                        textRes = R.string.clear,
                        iconRes = R.drawable.ic_close
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SearchTypeChipList(
                    modifier = Modifier.fillMaxWidth(),
                    onChipSelected = { selectedType ->
                        onAction(SearchTypeAction.OnTypeClick(selectedType))
                    },
                    selectedChip = type
                )
                Spacer(modifier = Modifier.height(8.dp))

                val currentSelectedItem = when (type) {
                    SearchType.ARTIST -> artistSearchItems
                    else -> albumSearchItems
                }

                BasePagingList(
                    isActiveSearch = activeSearchQuery.isNotBlank(),
                    items = currentSelectedItem,
                    listContent = { items ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            state = state
                        ) {
                            items(
                                count = items.itemCount,
                                contentType = { type.name },
                                key = { index ->
                                    when (val item = items.peek(index)) {
                                        is ArtistItem -> "artist_${item.id}"
                                        is AlbumItem -> "album_${item.id}"
                                        else -> "placeholder_$index"
                                    }
                                }
                            ) { index ->
                                val item = items[index]

                                when (type) {
                                    SearchType.ARTIST -> {
                                        (item as? ArtistItem)?.let { artist ->
                                            ArtistCard(
                                                item = artist,
                                                onArtistClick = {}
                                            )
                                        }
                                    }
                                    else -> {
                                        (item as? AlbumItem)?.let { album ->
                                            AlbumCard(
                                                item = album,
                                                onAlbumClick = { onAction(SearchTypeAction.OnAlbumClick(item.id)) }
                                            )
                                        }
                                    }
                                }
                            }

                            item (span = { GridItemSpan(maxLineSpan) }) {
                                val appendState = items.loadState.append
                                if (appendState is LoadState.Loading) {
                                    ContainedLoadingIndicator()
                                } else if (appendState is LoadState.Error) {
                                    ErrorItem(
                                        onRetry = { items.retry() },
                                        message = appendState.error.message ?: "Loading error"
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SearchTypeChipList(
    modifier: Modifier = Modifier,
    onChipSelected: (SearchType) -> Unit,
    selectedChip: SearchType
) {

    val chipList = SearchType.entries.map { type -> type.getLabel() to type }

    CenteredContent(modifier = modifier) { contentModifier ->
        LazyRow(modifier = contentModifier.padding(horizontal = 5.dp)) {
            items(chipList) { (label, sortKey) ->
                ChipItem(
                    selected = selectedChip == sortKey,
                    label = label,
                    onSelect = {
                        onChipSelected(sortKey)
                    },
                    modifier = Modifier.padding(horizontal = 5.dp),
                    isTrailingIcon = true
                )
            }
        }
    }
}

@Composable
fun SearchType.getLabel(): String {
    return when (this) {
        //SearchType.ALL -> stringResource(id = R.string.all)
        SearchType.ALBUM -> stringResource(id = R.string.album)
        SearchType.ARTIST -> stringResource(id = R.string.artist)
    }
}

@Composable
fun SearchBarTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    searchQuery: String,
    onSearch: () -> Unit
) {
    val searchFieldBackground = RoundedCornerShape(100.dp)
    val containerColor = MaterialTheme.colorScheme.surfaceContainer

    Row(
        modifier = modifier
            .height(36.dp)
            .clip(searchFieldBackground)
            .background(containerColor)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(size = 20.dp)
        )
        Spacer(modifier = modifier.width(10.dp))
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = searchQuery,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.padding(horizontal = 5.dp)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Start,
                                text = stringResource(R.string.search),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch() }
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
            )
        }
    }
}