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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.m4ykey.albly.R
import com.m4ykey.albly.album.presentation.components.AlbumCard
import com.m4ykey.albly.util.CenteredContent
import com.m4ykey.albly.util.chip.ChipItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onNavBack: () -> Unit,
    viewModel : SearchViewModel = hiltViewModel()
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

    val viewState by viewModel.searchType.collectAsState()
    val type by rememberUpdatedState(viewState.type)

    val onAction by rememberUpdatedState(newValue = viewModel::onAction)

    LaunchedEffect(Unit) {
        viewModel.typeUiEvent.collectLatest { event ->
            when (event) {
                is SearchUiEvent.ChangeType -> viewModel.updateType(event.type)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavBack) {
                Icon(
                    contentDescription = stringResource(R.string.back),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    .padding(start = 8.dp)
            )
            IconButton(onClick = { }) {
                Icon(
                    contentDescription = stringResource(R.string.mic),
                    imageVector = Icons.Outlined.Mic,
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
                        imageVector = Icons.Outlined.Clear,
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchItems.itemCount) { index ->
                    searchItems[index]?.let { item ->
                        AlbumCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchTypeChipList(
    modifier: Modifier = Modifier,
    onChipSelected : (SearchType) -> Unit,
    selectedChip : SearchType
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
fun SearchType.getLabel() : String {
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
    searchQuery: String
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
            imageVector = Icons.Default.Search,
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
                )
            )
        }
    }
}