@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.albly.album.presentation.new_release

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.albly.R
import com.m4ykey.albly.album.presentation.components.AlbumCard
import com.m4ykey.albly.util.paging.BasePagingList
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlbumNewReleaseScreen(
    onBack : () -> Unit,
    viewModel: NewReleaseViewModel = koinViewModel()
) {

    val newRelease = viewModel.newRelease.collectAsLazyPagingItems()

    val state = rememberLazyGridState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Release") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            contentDescription = stringResource(id = R.string.back),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        BasePagingList(
            items = newRelease,
            listContent = { items ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .padding(innerPadding)
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
                                onAlbumClick = {  }
                            )
                        }
                    }
                }
            }
        )
    }
}