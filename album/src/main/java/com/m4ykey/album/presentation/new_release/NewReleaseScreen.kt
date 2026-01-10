@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.album.presentation.new_release

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.album.R
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.core.model.domain.AlbumItem
import com.m4ykey.core.paging.BasePagingList
import com.m4ykey.core.ui.AlbumCard
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumNewReleaseScreen(
    onBack : () -> Unit,
    viewModel: NewReleaseViewModel = koinViewModel(),
    onAlbumClick : (String) -> Unit
) {

    val newRelease = viewModel.newRelease.collectAsLazyPagingItems()

    val state = rememberLazyGridState()

    val onAction = viewModel::onAction

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is NewReleaseUiEvent.OnAlbumClick -> onAlbumClick(event.id)
            }
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
}

@Composable
fun NewReleaseContent(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    state : LazyGridState,
    newRelease : LazyPagingItems<AlbumItem>,
    onAction : (NewReleaseAction) -> Unit
) {
    BasePagingList(
        items = newRelease,
        listContent = { items ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier
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
                    key = items.itemKey { item -> item.id }
                ) { index ->
                    items[index]?.let { item ->
                        AlbumCard(
                            item = item,
                            onAlbumClick = { onAction(NewReleaseAction.OnAlbumClick(item.id)) }
                        )
                    }
                }
            }
        }
    )
}