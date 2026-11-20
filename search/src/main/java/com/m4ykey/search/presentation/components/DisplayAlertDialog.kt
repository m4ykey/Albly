@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.search.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.m4ykey.album.presentation.components.SaveButtonRow
import com.m4ykey.album.presentation.detail.DetailUiState
import com.m4ykey.core.ui.AnimationCard
import com.m4ykey.core.ui.LoadImage
import com.m4ykey.search.R

@Composable
fun DisplayDeleteSearchItem(
    modifier: Modifier = Modifier,
    onDismissDialog : () -> Unit,
    removeOneElement : Boolean,
    item : String? = null,
    onDelete : () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        Surface(
            tonalElevation = 6.dp,
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (removeOneElement) stringResource(R.string.delete_this_item)
                    else stringResource(R.string.delete_all_items)
                )
                Text(
                    text = if (removeOneElement) "${stringResource(R.string.delete)}: $item"
                    else stringResource(R.string.delete_all_items_from_list)
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissDialog) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = { onDelete() }
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayAlbumCard(
    modifier: Modifier = Modifier,
    onDismissDialog : () -> Unit,
    state : DetailUiState,
    albumId : Int?,
    onAlbumClick: (Int) -> Unit,
    onToggleSave : () -> Unit,
    onToggleListenLater : () -> Unit,
    onCoverClick : (String) -> Unit
) {
    if (albumId == null) return

    val lazyListState = rememberLazyListState()

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val textColor2 = if (isSystemInDarkTheme()) Color(0xFFBDBDBD) else Color(0xFF424242)

    BasicAlertDialog(
        onDismissRequest = onDismissDialog,
        properties = DialogProperties(dismissOnBackPress = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            modifier = modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (state) {
                    is DetailUiState.Error -> {
                        AnimationCard(text = state.message)
                    }
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
                        LazyColumn(
                            modifier = Modifier.padding(16.dp),
                            state = lazyListState
                        ) {
                            val item = state.item

                            val genres = item.genres.joinToString(", ") { it }
                            val styles = item.styles.joinToString(", ") { it }

                            val imageUrl = item.images.find { it.type == "primary" }?.uri ?:
                            item.images.firstOrNull()?.uri

                            val artists = item.artists.joinToString(", ") { it.name }

                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadImage(
                                        modifier = Modifier
                                            .clickable { onCoverClick(imageUrl.orEmpty()) }
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
                                            "${stringResource(com.m4ykey.album.R.string.tracks)}: ${item.tracklist.size}",
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
                    onToggleSave = onToggleSave,
                    onToggleListenLater = onToggleListenLater,
                    state = state,
                    onDismissDialog = onDismissDialog,
                    onAlbumClick = onAlbumClick,
                    albumId = albumId
                )
            }
        }
    }
}

@Composable
fun ButtonsRow(
    modifier: Modifier = Modifier,
    textColor : Color,
    state : DetailUiState,
    onToggleSave : () -> Unit,
    onToggleListenLater: () -> Unit,
    onDismissDialog: () -> Unit,
    onAlbumClick: (Int) -> Unit,
    albumId : Int
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onDismissDialog) {
            Text(
                text = stringResource(R.string.close),
                color = textColor
            )
        }
        if (state is DetailUiState.Success) {
            SaveButtonRow(
                isSaved = state.isSaved,
                isListenLaterSaved = state.isListenLaterSaved,
                onSaveClick = onToggleSave,
                onListenLaterClick = onToggleListenLater,
                modifier = Modifier.weight(1f),
                divider = true
            )
            IconButton(onClick = { onAlbumClick(albumId) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                    contentDescription = stringResource(R.string.view_album)
                )
            }
        }
    }
}