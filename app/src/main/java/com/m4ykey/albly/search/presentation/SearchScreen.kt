package com.m4ykey.albly.search.presentation

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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.albly.R
import com.m4ykey.albly.album.presentation.components.AlbumCard
import com.m4ykey.core.ext.CenteredContent
import com.m4ykey.core.chip.ChipItem
import com.m4ykey.core.paging.BasePagingList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
    onAlbumClick : (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isDarkTheme = if (isSystemInDarkTheme()) Color.White else Color.Black
    val searchItems = viewModel.searchResults.collectAsLazyPagingItems()

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

    LaunchedEffect(viewModel) {
        viewModel.searchUiEvent.collectLatest { event ->
            when (event) {
                is SearchUiEvent.ChangeType -> viewModel.updateType(event.type)
                is SearchUiEvent.OnAlbumClick -> onAlbumClick(event.id)
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
                IconButton(onClick = onBack) {
                    Icon(
                        contentDescription = stringResource(R.string.back),
                        painter = painterResource(R.drawable.ic_arrow_left),
                        tint = isDarkTheme
                    )
                }
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
                IconButton(onClick = { }) {
                    Icon(
                        contentDescription = stringResource(R.string.mic),
                        painter = painterResource(R.drawable.ic_mic),
                        tint = isDarkTheme
                    )
                }
                AnimatedVisibility(
                    visible = searchQuery.isNotEmpty(),
                    enter = slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth }
                    )
                ) {
                    IconButton(onClick = {
                        onAction(SearchTypeAction.OnQueryChange(""))
                    }) {
                        Icon(
                            contentDescription = stringResource(R.string.clear),
                            painter = painterResource(R.drawable.ic_close),
                            tint = isDarkTheme
                        )
                    }
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
                BasePagingList(
                    isActiveSearch = activeSearchQuery.isNotBlank(),
                    items = searchItems,
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
                                contentType = { "album_item" },
                                key = items.itemKey { item -> item.id }
                            ) { index ->
                                items[index]?.let { item ->
                                    AlbumCard(
                                        item = item,
                                        onAlbumClick = { onAction(SearchTypeAction.OnAlbumClick(item.id)) }
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
            tint = MaterialTheme.colorScheme.onSurfaceVariant
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