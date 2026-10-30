@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.album.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.album.R
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.mapper.toAlbumEntity
import com.m4ykey.album.domain.model.TrackClickItem
import com.m4ykey.album.domain.model.detail.AlbumRoot
import com.m4ykey.album.domain.model.detail.Artists
import com.m4ykey.album.presentation.components.AlbumButtonRow
import com.m4ykey.album.presentation.components.SaveButtonRow
import com.m4ykey.album.presentation.components.TrackListItem
import com.m4ykey.artist.domain.model.Artist
import com.m4ykey.artist.presentation.ArtistState
import com.m4ykey.artist.presentation.ArtistViewModel
import com.m4ykey.core.network.openLink
import com.m4ykey.core.ui.ActionIconButton
import com.m4ykey.core.ui.AppScaffold
import com.m4ykey.core.ui.ErrorCard
import com.m4ykey.core.ui.LoadImage
import com.m4ykey.core.ui.copyText
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumDetailScreen(
    id : Int,
    onBack : () -> Unit,
    viewModel : AlbumDetailViewModel = koinViewModel(),
    onTrackClick : (TrackClickItem) -> Unit,
    onCoverClick: (String) -> Unit,
    onArtistClick: (Int) -> Unit,
    artistViewModel : ArtistViewModel = koinViewModel()
) {

    val albumDetail by viewModel.detail.collectAsStateWithLifecycle()

    val state = rememberLazyListState()

    LaunchedEffect(id) {
        viewModel.getAlbumById(id)
    }

    AppScaffold(
        navigation = {
            ActionIconButton(
                onClick = onBack,
                textRes = R.string.back,
                iconRes = R.drawable.ic_arrow_left
            )
        },
        actions = {
            ActionIconButton(
                onClick = {},
                textRes = R.string.info,
                iconRes = R.drawable.ic_info
            )
        },
        content = { padding ->
            AlbumDetailDisplay(
                onTrackClick = onTrackClick,
                state = state,
                albumDetail = albumDetail,
                paddingValues = padding,
                onSaveToggle = { entity ->
                    viewModel.toggleSave(album = entity)
                },
                onListenLaterToggle = { entity ->
                    viewModel.toggleListenLater(album = entity)
                },
                onCoverClick = onCoverClick,
                onArtistClick = onArtistClick,
                artistViewModel = artistViewModel
            )
        }
    )
}

@Composable
fun AlbumDetailDisplay(
    modifier: Modifier = Modifier,
    albumDetail : DetailUiState,
    paddingValues: PaddingValues,
    onTrackClick: (TrackClickItem) -> Unit,
    state : LazyListState,
    onSaveToggle: (AlbumEntity) -> Unit,
    onListenLaterToggle: (AlbumEntity) -> Unit,
    onCoverClick: (String) -> Unit,
    onArtistClick : (Int) -> Unit,
    artistViewModel : ArtistViewModel
) {
    when (albumDetail) {
        is DetailUiState.Loading -> {
            Box(modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                ContainedLoadingIndicator()
            }
        }
        is DetailUiState.Error -> {
            ErrorCard(text = albumDetail.message)
        }
        is DetailUiState.Success -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AlbumDetailContent(
                    item = albumDetail.item,
                    contentPadding = paddingValues,
                    state = state,
                    onTrackClick = onTrackClick,
                    onSaveToggle = onSaveToggle,
                    onListenLaterToggle = onListenLaterToggle,
                    isSaved = albumDetail.isSaved,
                    isListenLaterSaved = albumDetail.isListenLaterSaved,
                    onCoverClick = onCoverClick,
                    onArtistClick = onArtistClick,
                    artistViewModel = artistViewModel
                )
            }
        }
    }
}

@Composable
fun AlbumDetailContent(
    contentPadding : PaddingValues = PaddingValues(0.dp),
    state : LazyListState,
    onTrackClick : (TrackClickItem) -> Unit,
    onSaveToggle : (AlbumEntity) -> Unit,
    onListenLaterToggle : (AlbumEntity) -> Unit,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    item : AlbumRoot,
    onCoverClick : (String) -> Unit,
    onArtistClick : (Int) -> Unit,
    artistViewModel : ArtistViewModel
) {
    val albumEntity = remember(item) {
        item.toAlbumEntity()
    }

    val imageUrl = item.images.find { it.type == "primary" }?.uri ?: item.images.firstOrNull()?.uri
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    val albumInfo = "${item.year} • ${item.tracklist.size} " +
            stringResource(R.string.tracks)

    val artists = item.artists.joinToString(", ") { it.name }

    val context = LocalContext.current
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    if (showAlertDialog) {
        ShowArtistDialog(
            onArtistClick = { artist ->
                showAlertDialog = false
                onArtistClick(artist)
            },
            onDismiss = { showAlertDialog = false },
            state = artistViewModel.artistDialogState.collectAsState().value
        )
    }

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
                    imageUrl = imageUrl.orEmpty(),
                    modifier = Modifier
                        .size(260.dp)
                        .clickable { onCoverClick(imageUrl.orEmpty()) }
                )
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.title,
                    fontSize = 25.sp,
                    color = textColor,
                    modifier = Modifier.clickable {
                        copyText(text = item.title, context)
                    }
                )
                Text(
                    text = artists,
                    fontSize = 15.sp,
                    color = textColor2
                )
            }
        }

        item {
            SaveButtonRow(
                isSaved = isSaved,
                isListenLaterSaved = isListenLaterSaved,
                onSaveClick = {
                    onSaveToggle(albumEntity)
                },
                onListenLaterClick = {
                    onListenLaterToggle(albumEntity)
                }
            )
        }

        item {
            Text(
                text = albumInfo,
                color = textColor2,
                fontSize = 14.sp
            )
        }

        item {
            AlbumButtonRow(
                onAlbumClick = {
                    openLink(item.uri, context)
                },
                onArtistClick = {
                    when (item.artists.size) {
                        0 -> Unit
                        1 -> onArtistClick(item.artists[0].id)
                        else -> {
                            showAlertDialog = true
                            artistViewModel.loadArtists(item.artists.map { it.id })
                        }
                    }
                }
            )
        }

        items(
            count = item.tracklist.size,
            key = { index -> item.tracklist[index].position },
            contentType = { "track_item" }
        ) { index ->
            val currentDuration = item.tracklist[index].duration
            val currentTitle = item.tracklist[index].title
            val currentPosition = item.tracklist[index].position

            TrackListItem(
                onTrackClick = onTrackClick,
                duration = currentDuration,
                title = currentTitle,
                artists = artists,
                position = currentPosition,
                img = imageUrl.orEmpty(),
                album = item.title
            )
        }
    }
}

@Composable
fun ShowArtistDialog(
    state : ArtistState?,
    onDismiss : () -> Unit,
    onArtistClick : (Int) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.choose_artist),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                when (state) {
                    null, ArtistState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is ArtistState.Error -> {
                        Text(state.message)
                    }
                    is ArtistState.Success -> {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(state.artists) { artist ->
                                AlbumArtistItem(
                                    artist = artist,
                                    onArtistClick = onArtistClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumArtistItem(
    modifier: Modifier = Modifier,
    artist : Artist,
    onArtistClick: (Int) -> Unit
) {
    val imageUrl = artist.images.find { it.type == "primary" }?.uri ?: artist.images.firstOrNull()?.uri

    Row(
        modifier = modifier
            .clickable { onArtistClick(artist.id) }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadImage(
            imageUrl = imageUrl.orEmpty(),
            modifier = Modifier.size(50.dp)
        )
        Text(text = artist.name)
    }
}