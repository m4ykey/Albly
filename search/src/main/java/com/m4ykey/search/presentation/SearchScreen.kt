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
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.m4ykey.album.presentation.detail.AlbumDetailAction
import com.m4ykey.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.core.database.model.search.SearchItem
import com.m4ykey.core.paging.ErrorItem
import com.m4ykey.core.ui.AnimationCard
import com.m4ykey.search.R
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.model.lyrics.Hit
import com.m4ykey.search.presentation.components.AlbumSearchList
import com.m4ykey.search.presentation.components.ArtistSearchList
import com.m4ykey.album.presentation.components.DisplayAlbumCard
import com.m4ykey.album.presentation.components.DisplayDeleteSearchItem
import com.m4ykey.search.presentation.components.LyricsSearchList
import com.m4ykey.search.presentation.components.SearchBar
import com.m4ykey.search.presentation.components.SearchItemList
import com.m4ykey.search.presentation.components.SearchTypeChipList
import org.koin.compose.viewmodel.koinViewModel
import java.util.Locale

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
    onAlbumClick : (Int) -> Unit,
    onTrackClick : (String, String, String, String?) -> Unit,
    onArtistClick : (Int) -> Unit,
    albumViewModel : AlbumDetailViewModel = koinViewModel(),
    onCoverClick: (String) -> Unit
) {
    val isDarkTheme = if (isSystemInDarkTheme()) Color.White else Color.Black

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val albumUiState by albumViewModel.uiState.collectAsStateWithLifecycle()
    val recentSearch by viewModel.recentSearches.collectAsStateWithLifecycle()

    val albumItems = viewModel.searchAlbumResults.collectAsLazyPagingItems()
    val artistItems = viewModel.searchArtistResults.collectAsLazyPagingItems()
    val lyricsItems = viewModel.searchLyricsResults.collectAsLazyPagingItems()

    val onAction = viewModel::onAction

    val albumGridState = rememberLazyGridState()
    val artistListState = rememberLazyListState()
    val lyricsListState = rememberLazyListState()

    val context = LocalContext.current

    val type = uiState.type
    val searchQuery = uiState.query

    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is SearchEffect.OnAlbumClick -> onAlbumClick(effect.id)
                    is SearchEffect.OnTrackClick -> onTrackClick(effect.title, effect.artist, effect.img, effect.album)
                    is SearchEffect.OnArtistClick -> onArtistClick(effect.id)
                }
            }
        }
    }

    LaunchedEffect(type) {
        when (type) {
            SearchType.LYRICS -> lyricsListState.scrollToItem(0)
            SearchType.ARTIST -> artistListState.scrollToItem(0)
            SearchType.ALBUM -> albumGridState.scrollToItem(0)
        }
    }

    LaunchedEffect(uiState.selectedAlbumId) {
        uiState.selectedAlbumId?.let { id ->
            albumViewModel.onAction(AlbumDetailAction.OnLoadAlbum(id))
        }
    }

    val permissionState = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = matches?.getOrNull(0) ?: ""

            if (!matches.isNullOrEmpty()) {
                onAction(SearchAction.OnQueryChange(spokenText))
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
        } catch (_ : Exception) {
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

    val isActiveSearch = uiState.activeQuery.isNotBlank()

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                isDarkTheme = isDarkTheme,
                onBack = onBack,
                searchQuery = searchQuery,
                onAction = onAction,
                handleMick = handleMick
            )
            SearchContent(
                onAction = onAction,
                isActiveSearch = isActiveSearch,
                albumItems = albumItems,
                type = type,
                artistItems = artistItems,
                lyricsItems = lyricsItems,
                artistListState = artistListState,
                albumGridState = albumGridState,
                lyricsListState = lyricsListState,
                recentSearch = recentSearch
            )
        }
    }

    DisplayAlbumCard(
        onDismissDialog = { onAction(SearchAction.OnHideAlbumDialog) },
        state = albumUiState,
        albumId = uiState.selectedAlbumId,
        onAlbumClick = { id ->
            onAction(SearchAction.OnHideAlbumDialog)
            onAlbumClick(id)
        },
        onToggleSave = { albumViewModel.onAction(AlbumDetailAction.OnToggleSave) },
        onToggleListenLater = { albumViewModel.onAction(AlbumDetailAction.OnToggleListenLater) },
        onCoverClick = { img ->
            onAction(SearchAction.OnHideAlbumDialog)
            onCoverClick(img)
        }
    )

    val deleteTarget = uiState.deleteTarget
    if (deleteTarget != null) {
        DisplayDeleteSearchItem(
            onDismissDialog = { onAction(SearchAction.OnHideSearchItemDialog) },
            removeOneElement = deleteTarget is DeleteSearchTarget.Single,
            onDelete = { onAction(SearchAction.OnConfirmDeleteSearchItem) },
            item = (deleteTarget as? DeleteSearchTarget.Single)?.item?.text
        )
    }
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    onAction: (SearchAction) -> Unit,
    isActiveSearch: Boolean,
    albumItems : LazyPagingItems<ResultsAlbum>,
    type : SearchType,
    artistItems : LazyPagingItems<ResultsArtist>,
    lyricsItems : LazyPagingItems<Hit>,
    artistListState : LazyListState,
    albumGridState : LazyGridState,
    lyricsListState : LazyListState,
    recentSearch : List<SearchItem>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SearchTypeChipList(
            modifier = Modifier.fillMaxWidth(),
            onChipSelected = { selectedType ->
                onAction(SearchAction.OnTypeClick(selectedType))
            },
            selectedChip = type
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (!isActiveSearch) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.recent_search),
                    modifier = Modifier.weight(1f)
                )
                if (recentSearch.isNotEmpty()) {
                    IconButton(
                        onClick = { onAction(SearchAction.OnDeleteAllSearchItemsClick) },
                        modifier = Modifier.then(Modifier.size(22.dp))
                    ) {
                        Icon(
                            contentDescription = stringResource(R.string.delete_everything),
                            painter = painterResource(R.drawable.ic_delete)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            if (recentSearch.isEmpty()) {
                AnimationCard(
                    showText = true,
                    animation = com.m4ykey.core.R.raw.cat_empty_list,
                    text = stringResource(R.string.why_nothing_here),
                    size = 300.dp
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(
                        items = recentSearch,
                        key = { item -> item.id }
                    ) { item ->
                        SearchItemList(
                            text = item.text,
                            onClick = {
                                onAction(SearchAction.OnQueryChange(item.text))
                                onAction(SearchAction.OnSearchClick)
                            },
                            onLongClick = { onAction(SearchAction.OnSearchItemLongClick(item)) },
                            onSetText = { onAction(SearchAction.OnQueryChange(item.text)) }
                        )
                    }
                }
            }
        } else {
            when (type) {
                SearchType.ALBUM -> AlbumSearchList(
                    items = albumItems,
                    isActiveSearch = isActiveSearch,
                    state = albumGridState,
                    onAction = onAction
                )

                SearchType.ARTIST -> ArtistSearchList(
                    isActiveSearch = isActiveSearch,
                    items = artistItems,
                    state = artistListState,
                    onAction = onAction
                )

                SearchType.LYRICS -> LyricsSearchList(
                    isActiveSearch = isActiveSearch,
                    onAction = onAction,
                    items = lyricsItems,
                    state = lyricsListState
                )
            }
        }
    }
}

@Composable
fun PagingAppendState(items : LazyPagingItems<*>) {
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