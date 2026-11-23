@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.album.presentation.new_release

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.album.R
import com.m4ykey.album.R.string.tracks
import com.m4ykey.album.domain.model.new_release.NewReleaseResult
import com.m4ykey.album.presentation.components.ButtonsRow
import com.m4ykey.album.presentation.detail.AlbumDetailAction
import com.m4ykey.album.presentation.detail.AlbumDetailViewModel
import com.m4ykey.album.presentation.detail.DetailUiState
import com.m4ykey.core.paging.BasePagingList
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AlbumCard
import com.m4ykey.core.ui.AnimationCard
import com.m4ykey.core.ui.AppAlertDialog
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.core.ui.LoadImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumNewReleaseScreen(
    onBack : () -> Unit,
    viewModel: NewReleaseViewModel = koinViewModel(),
    onAlbumClick : (Int) -> Unit,
    albumViewModel : AlbumDetailViewModel = koinViewModel(),
    onCoverClick : (String) -> Unit
) {

    val newRelease = viewModel.newRelease.collectAsLazyPagingItems()
    val detailUiState by albumViewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val state = rememberLazyGridState()
    val lazyListState = rememberLazyListState()

    val onAction = viewModel::onAction
    val lifecycle = LocalLifecycleOwner.current

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    LaunchedEffect(viewModel, lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is NewReleaseEffect.OnAlbumClick -> onAlbumClick(event.id)
                }
            }
        }
    }

    LaunchedEffect(uiState.selectedAlbumId) {
        uiState.selectedAlbumId?.let { id ->
            albumViewModel.onAction(AlbumDetailAction.OnLoadAlbum(id))
        }
    }

    AppScaffold(
        title = R.string.discover_new_release,
        navigation = {
            ActionIconButton(
                onClick = onBack,
                iconRes = R.drawable.ic_arrow_left,
                textRes = R.string.back
            )
        },
        content = { padding ->
            NewReleaseContent(
                paddingValues = padding,
                state = state,
                newRelease = newRelease,
                onAction = onAction
            )
        }
    )

    uiState.selectedAlbumId?.let { id ->
        AppAlertDialog(
            onDismissRequest = { onAction(NewReleaseAction.OnHideAlbumDialog) },
            content = {
                when (val state = detailUiState) {
                    is DetailUiState.Error -> AnimationCard(text = state.message)
                    is DetailUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .height(160.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ContainedLoadingIndicator()
                        }
                    }
                    is DetailUiState.Success -> {
                        val item = state.item

                        val genres = item.genres.joinToString(", ") { it }
                        val styles = item.styles.joinToString(", ") { it }

                        val imageUrl = item.images.find { it.type == "primary" }?.uri ?:
                        item.images.firstOrNull()?.uri

                        val artists = item.artists.joinToString(", ") { it.name }

                        LazyColumn(
                            modifier = Modifier.padding(16.dp),
                            state = lazyListState
                        ) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadImage(
                                        modifier = Modifier
                                            .clickable {
                                                onCoverClick(imageUrl.orEmpty())
                                                onAction(NewReleaseAction.OnHideAlbumDialog)
                                            }
                                            .size(120.dp),
                                        imageUrl = imageUrl.orEmpty()
                                    )
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            item {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Album: ${item.title}",
                                        color = textColor,
                                        fontSize = 17.sp
                                    )
                                    Text(
                                        text = "${stringResource(R.string.artist)}: $artists",
                                        color = textColor2,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                            if (genres.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "${stringResource(R.string.genres)}: $genres",
                                        color = textColor2,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            if (styles.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "${stringResource(R.string.styles)}: $styles",
                                        color = textColor2,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            item {
                                Text(
                                    text = "${stringResource(R.string.year)}: ${item.year} • " +
                                            "${stringResource(tracks)}: ${item.tracklist.size}",
                                    color = textColor2,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ButtonsRow(
                    textColor = textColor,
                    onToggleSave = { albumViewModel.onAction(AlbumDetailAction.OnToggleSave) },
                    onToggleListenLater = { albumViewModel.onAction(AlbumDetailAction.OnToggleListenLater) },
                    state = detailUiState,
                    onDismissDialog = { onAction(NewReleaseAction.OnHideAlbumDialog) },
                    onAlbumClick = { id ->
                        onAction(NewReleaseAction.OnHideAlbumDialog)
                        onAlbumClick(id)
                    },
                    albumId = id
                )
            }
        )
    }
}

@Composable
fun NewReleaseContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state : LazyGridState,
    newRelease : LazyPagingItems<NewReleaseResult>,
    onAction : (NewReleaseAction) -> Unit
) {
    BasePagingList(
        items = newRelease,
        listContent = { items ->
            val isRefreshing = items.itemCount > 0 && (
                    items.loadState.refresh is LoadState.Loading ||
                    items.loadState.mediator?.refresh is LoadState.Loading
                    )

            PullToRefreshBox(
                modifier = modifier.fillMaxSize(),
                isRefreshing = isRefreshing,
                onRefresh = { items.refresh() }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = state
                ) {
                    items(
                        count = items.itemCount,
                        contentType = { "album_item" },
                        key = items.itemKey { item -> item.masterId }
                    ) { index ->
                        items[index]?.let { item ->
                            AlbumCard(
                                albumName = item.title,
                                image = item.thumb.ifBlank { item.coverImage },
                                artistName = item.artist,
                                onLongAlbumClick = {
                                    onAction(NewReleaseAction.OnLongAlbumClick(item.masterId))
                                },
                                onAlbumClick = { onAction(NewReleaseAction.OnAlbumClick(item.masterId)) }
                            )
                        }
                    }
                }
            }
        }
    )
}