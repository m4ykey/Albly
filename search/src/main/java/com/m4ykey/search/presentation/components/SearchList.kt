package com.m4ykey.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.m4ykey.core.paging.BasePagingList
import com.m4ykey.core.ui.AlbumCard
import com.m4ykey.core.ui.ArtistCard
import com.m4ykey.core.ui.LyricsCard
import com.m4ykey.search.domain.model.album.ResultsAlbum
import com.m4ykey.search.domain.model.artist.ResultsArtist
import com.m4ykey.search.domain.model.lyrics.Hit
import com.m4ykey.search.presentation.PagingAppendState
import com.m4ykey.search.presentation.SearchAction

@Composable
fun ArtistSearchList(
    modifier: Modifier = Modifier,
    isActiveSearch : Boolean,
    items : LazyPagingItems<ResultsArtist>,
    state : LazyListState,
    onAction : (SearchAction) -> Unit
) {
    BasePagingList(
        isActiveSearch = isActiveSearch,
        items = items,
        listContent = { items ->
            val isRefreshing = items.loadState.refresh is LoadState.Loading && items.itemCount > 0

            PullToRefreshBox(
                modifier = modifier.fillMaxSize(),
                onRefresh = { items.refresh() },
                isRefreshing = isRefreshing
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = state
                ) {
                    items(
                        count = items.itemCount,
                        contentType = { "artist_item" },
                        key = { index -> items.peek(index)?.id?.let { "artist_$it" } ?: "placeholder_album_$index" }
                    ) { index ->
                        items[index]?.let { artist ->
                            ArtistCard(
                                onArtistClick = {
                                    onAction(
                                        SearchAction.OnArtistClick(artist.id)
                                    )
                                },
                                title = artist.title,
                                cover = artist.thumb.ifBlank { artist.cover_image },
                                id = artist.id
                            )
                        }
                    }

                    item  {
                        PagingAppendState(items)
                    }
                }
            }
        }
    )
}

@Composable
fun LyricsSearchList(
    modifier: Modifier = Modifier,
    isActiveSearch: Boolean,
    onAction: (SearchAction) -> Unit,
    state : LazyListState,
    items : LazyPagingItems<Hit>
) {
    BasePagingList(
        isActiveSearch = isActiveSearch,
        items = items,
        listContent = { items ->
            val isRefreshing = items.loadState.refresh is LoadState.Loading && items.itemCount > 0

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                modifier = modifier.fillMaxSize(),
                onRefresh = { items.refresh() }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    state = state
                ) {
                    items(
                        contentType = { "lyrics_item" },
                        key = { index -> items.peek(index)?.isrc?.let { "lyrics_$it" } ?: "placeholder_lyrics_$index" },
                        count = items.itemCount
                    ) { index ->
                        items[index]?.let { lyrics ->
                            LyricsCard(
                                artist = lyrics.artist,
                                title = lyrics.title,
                                imageUrl = lyrics.cover,
                                onClick = {
                                    onAction(
                                        SearchAction.OnTrackClick(
                                            title = lyrics.title,
                                            img = lyrics.cover,
                                            artist = lyrics.artist,
                                            album = null
                                        )
                                    )
                                }
                            )
                        }
                    }

                    item {
                        PagingAppendState(items)
                    }
                }
            }
        }
    )
}

@Composable
fun AlbumSearchList(
    modifier: Modifier = Modifier,
    items : LazyPagingItems<ResultsAlbum>,
    isActiveSearch : Boolean,
    state : LazyGridState,
    onAction : (SearchAction) -> Unit
) {
    BasePagingList(
        isActiveSearch = isActiveSearch,
        items = items,
        listContent = { items ->
            val isRefreshing = items.loadState.refresh is LoadState.Loading && items.itemCount > 0

            PullToRefreshBox(
                modifier = modifier.fillMaxSize(),
                onRefresh = { items.refresh() },
                isRefreshing = isRefreshing
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = state
                ) {
                    items(
                        count = items.itemCount,
                        contentType = { "album_item" },
                        key = { index -> items.peek(index)?.id?.let { "album_$it" } ?: "placeholder_album_$index" }
                    ) { index ->
                        items[index]?.let { album ->
                            AlbumCard(
                                image = album.thumb?.takeIf { it.isNotBlank() }
                                    ?: album.cover_image.orEmpty(),
                                albumName = album.album.orEmpty(),
                                artistName = album.artist.orEmpty(),
                                onAlbumClick = {
                                    onAction(SearchAction.OnAlbumClick(album.id ?: 0)) },
                                onLongAlbumClick = {
                                    onAction(SearchAction.OnLongAlbumClick(album.id ?: 0))
                                }
                            )
                        }
                    }

                    item (span = { GridItemSpan(maxLineSpan) }) {
                        PagingAppendState(items)
                    }
                }
            }
        }
    )
}