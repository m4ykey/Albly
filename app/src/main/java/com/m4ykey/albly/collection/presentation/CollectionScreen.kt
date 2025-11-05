package com.m4ykey.albly.collection.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.m4ykey.albly.R
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.collection.presentation.type.album.AlbumTypeAction
import com.m4ykey.albly.collection.presentation.type.components.chip.ListTypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.SortTypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.ViewTypeChip
import com.m4ykey.albly.collection.presentation.type.list.ListSortType
import com.m4ykey.albly.collection.presentation.type.list.ListType
import com.m4ykey.albly.collection.presentation.type.list.ListViewType
import com.m4ykey.albly.util.CenteredContent
import com.m4ykey.albly.util.chip.ChipItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    modifier : Modifier = Modifier,
    onSearch : () -> Unit,
    viewModel : CollectionViewModel = viewModel(),
    onLinkClick : (String) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewState by viewModel.albumType.collectAsState()
    val type by rememberUpdatedState(viewState.type)

    var showDialog by remember { mutableStateOf(false) }
    val state = rememberTextFieldState()

    val onAction = viewModel::onAction

    val listState = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.collectionUiEvent.collectLatest { event ->
            when (event) {
                is CollectionUiEvent.ChangeType -> viewModel.updateType(event.type)
                is CollectionUiEvent.OnLinkClick -> onLinkClick(event.link)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.collection)) },
                actions = {
                    IconButton(
                        onClick = { onSearch() }
                    ) {
                        Icon(
                            contentDescription = stringResource(R.string.search),
                            imageVector = Icons.Default.Search
                        )
                    }
                    IconButton(
                        onClick = {
                            showDialog = true
                        }
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
            onAction = onAction,
            padding = padding,
            listState = listState
        )
    }

    if (showDialog) {
        AnimatedVisibility(
            visible = showDialog,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            BasicAlertDialog(
                onDismissRequest = { showDialog = false },
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 6.dp,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column {
                        UrlInputField(state = state)

                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { showDialog = false }
                            ) {
                                Text(stringResource(R.string.cancel))
                            }
                            TextButton(
                                onClick = { showDialog = false }
                            ) {
                                Text("Ok")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UrlInputField(
    state : TextFieldState
) {
    OutlinedTextField(
        state = state,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                contentDescription = null,
                imageVector = Icons.Default.Link
            )
        },
        label = { Text(stringResource(R.string.enter_url)) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        state.edit { replace(0, state.text.length, "") }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Uri
        )
    )
}

@Composable
fun CollectionScreenContent(
    modifier: Modifier = Modifier,
    type : AlbumType?,
    onAction : (AlbumTypeAction) -> Unit,
    padding : PaddingValues,
    listState: LazyListState
) {
    var sortType by rememberSaveable { mutableStateOf(ListSortType.LATEST) }
    var viewType by rememberSaveable { mutableStateOf(ListViewType.GRID) }
    var listType by rememberSaveable { mutableStateOf(ListType.ALBUM) }

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
                ListOptions(
                    modifier = modifier.padding(horizontal = 5.dp),
                    onSortChange = { sortType = it },
                    onViewChange = { viewType = it },
                    onListTypeChange = { listType = it }
                )
            }
        }
    }
}

@Composable
fun ListOptions(
    modifier: Modifier = Modifier,
    onSortChange : (ListSortType) -> Unit,
    onListTypeChange : (ListType) -> Unit,
    onViewChange : (ListViewType) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ListTypeChip(onChange = onListTypeChange)
        SortTypeChip(onChange = onSortChange)
        ViewTypeChip(onChange = onViewChange)
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
                    },
                    isLeadingIcon = true
                )
            }
        }
    }
}