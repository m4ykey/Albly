package com.m4ykey.albly.collection.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.m4ykey.albly.R
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.collection.presentation.type.album.AlbumTypeAction
import com.m4ykey.albly.collection.presentation.type.components.chip.ListTypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.SortTypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.ViewTypeChip
import com.m4ykey.albly.util.CenteredContent
import com.m4ykey.albly.util.chip.ChipItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(modifier : Modifier = Modifier) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewModel : CollectionViewModel = viewModel()
    val viewState by viewModel.albumType.collectAsState()
    val type by rememberUpdatedState(viewState.type)

    val currentOnAction by rememberUpdatedState(newValue = viewModel::onAction)

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(Unit) {
        viewModel.typeUiEvent.collectLatest { event ->
            when (event) {
                is CollectionUiEvent.ChangeType -> viewModel.updateType(event.type)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.collection)) },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            contentDescription = stringResource(R.string.search),
                            imageVector = Icons.Default.Search
                        )
                    }
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            contentDescription = stringResource(R.string.enter_url),
                            imageVector = Icons.Default.Link
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        CollectionScreenContent(
            type = type,
            onAction = currentOnAction,
            padding = padding,
            listState = listState
        )
    }
}

@Composable
fun CollectionScreenContent(
    modifier: Modifier = Modifier,
    type : AlbumType?,
    onAction : (AlbumTypeAction) -> Unit,
    padding : PaddingValues,
    listState: LazyListState
) {
    LazyColumn(
        modifier = modifier
            .padding(padding)
            .fillMaxSize(),
        state = listState
    ) {
        item {
            Column(
                modifier = modifier.fillMaxWidth()
            ) {
                AlbumTypeChipList(
                    selectedChip = type,
                    onChipSelected = { selectedType ->
                        onAction(AlbumTypeAction.OnTypeClick(selectedType))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                ListOptions()
            }
        }
    }
}

@Composable
fun ListOptions(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ListTypeChip(
            modifier = modifier.padding(horizontal = 5.dp)
        ) { newType -> }
        SortTypeChip(

        ) { newType -> }
        ViewTypeChip(

        ) { newType -> }
    }
}

@Composable
fun AlbumType.getLabel() : String {
    return when (this) {
        AlbumType.ALBUM -> stringResource(id = R.string.album)
        AlbumType.EP -> stringResource(id = R.string.ep)
        AlbumType.SINGLE -> stringResource(id = R.string.single)
        AlbumType.COMPILATION -> stringResource(id = R.string.compilation)
        AlbumType.EMPTY -> ""
    }
}

@Composable
fun AlbumTypeChipList(
    modifier : Modifier = Modifier,
    onChipSelected : (AlbumType?) -> Unit,
    selectedChip : AlbumType?
) {
    val chipList = AlbumType.entries
        .filter { it != AlbumType.EMPTY }
        .map { type -> type.getLabel() to type }

    CenteredContent(modifier = modifier) { contentModifier ->
        LazyRow(modifier = contentModifier.padding(horizontal = 5.dp)) {
            items(chipList) { (label, sortKey) ->
                ChipItem(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    selected = selectedChip == sortKey,
                    label = label,
                    onSelect = {
                        if (selectedChip == sortKey) {
                            onChipSelected(null)
                        } else {
                            onChipSelected(sortKey)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CollectionScreenPrev() {
    CollectionScreen()
}