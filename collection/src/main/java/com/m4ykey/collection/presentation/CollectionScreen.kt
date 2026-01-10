@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.collection.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.album.data.local.model.AlbumEntity
import com.m4ykey.album.data.mapper.toAlbum
import com.m4ykey.collection.R
import com.m4ykey.collection.model.DrawerIcon
import com.m4ykey.collection.model.DrawerItem
import com.m4ykey.collection.model.IconSource
import com.m4ykey.collection.presentation.components.AlbumListRow
import com.m4ykey.collection.presentation.components.AlbumTypeChipList
import com.m4ykey.collection.presentation.components.EmptyList
import com.m4ykey.collection.presentation.components.ListOptions
import com.m4ykey.collection.presentation.components.SearchField
import com.m4ykey.collection.presentation.components.UrlInputField
import com.m4ykey.collection.presentation.type.list.ListSortType
import com.m4ykey.collection.presentation.type.list.ListType
import com.m4ykey.collection.presentation.type.list.ListViewType
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import com.m4ykey.core.ext.showToast
import com.m4ykey.core.model.type.AlbumType
import com.m4ykey.core.ui.AlbumGridCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.net.URL

@Composable
fun CollectionScreen(
    modifier : Modifier = Modifier,
    onSearch : () -> Unit,
    viewModel : CollectionViewModel = koinViewModel(),
    onLinkClick : (String) -> Unit,
    onAlbumClick : (String) -> Unit,
    navigateToNewRelease : () -> Unit,
    navigateToListenLater : () -> Unit,
    navigateToSettings : () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val albumTypeState by viewModel.albumType.collectAsStateWithLifecycle()
    val currentType = albumTypeState.type

    val isSortDialogVisible by viewModel.isSortDialogVisible.collectAsState()
    val isSearchVisible by viewModel.isSearchVisible.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val isDialogVisible by viewModel.isLinkDialogVisible.collectAsState()
    val state = rememberTextFieldState()

    val onAction = viewModel::onAction

    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(-1) }

    val context = LocalContext.current

    val items = remember {
        listOf(
            DrawerItem(
                titleRes = R.string.new_release,
                icon = IconSource.Resource(R.drawable.ic_new_release),
                onClick = { navigateToNewRelease() }
            ),
            DrawerItem(
                titleRes = R.string.listen_later,
                icon = IconSource.Vector(Icons.Default.AccessTime),
                onClick = { navigateToListenLater() }
            ),
            DrawerItem(
                titleRes = R.string.settings,
                icon = IconSource.Vector(Icons.Outlined.Settings),
                onClick = { navigateToSettings() }
            )
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.collectionUiEvent.collectLatest { event ->
            when (event) {
                is CollectionUiEvent.OnLinkClick -> onLinkClick(event.link)
            }
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = modifier.width(300.dp)
            ) {
                Spacer(modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = stringResource(item.titleRes)) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch { drawerState.close() }
                            item.onClick()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        icon = {
                            DrawerIcon(
                                iconSource = item.icon,
                                contentDescription = stringResource(item.titleRes)
                            )
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        AppScaffold(
            modifier = modifier,
            navigation = {
                ActionIconButton(
                    textRes = R.string.menu,
                    iconRes = R.drawable.ic_menu,
                    onClick = { scope.launch { drawerState.open() } }
                )
            },
            actions = {
                ActionIconButton(
                    onClick = { onSearch() },
                    iconRes = R.drawable.ic_search,
                    textRes = R.string.search
                )
                ActionIconButton(
                    onClick = { viewModel.showLinkDialog() },
                    iconRes = R.drawable.ic_link,
                    textRes = R.string.enter_url
                )
            },
            title = R.string.collection,
            content = { padding ->
                CollectionScreenContent(
                    type = currentType,
                    onAction = onAction,
                    padding = padding,
                    listState = listState,
                    onSearchClick = { viewModel.showSearch() },
                    onShowSortDialog = { viewModel.showSortDialog() },
                    onDismissSortDialog = { viewModel.hideSortDialog() },
                    isSortDialogVisible = isSortDialogVisible,
                    isSearchVisible = isSearchVisible,
                    searchQuery = searchQuery,
                    onHideSearchClick = { viewModel.hideSearch() },
                    clearTextField = { viewModel.clearTextField() },
                    albums = albums,
                    onAlbumClick = onAlbumClick,
                    isLoading = isLoading,
                    gridState = gridState
                )
            },
            scrollBehavior = scrollBehavior
        )
    }

    if (isDialogVisible) {
        AnimatedVisibility(
            visible = isDialogVisible,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            BasicAlertDialog(
                onDismissRequest = { viewModel.hideLinkDialog() },
                properties = DialogProperties(dismissOnBackPress = true)
            ) {
                AlertDialogBody(
                    state = state,
                    onHideLinkDialogClick = { viewModel.hideLinkDialog() },
                    onLinkClick = onLinkClick,
                    context = context
                )
            }
        }
    }
}

@Composable
fun AlertDialogBody(
    modifier: Modifier = Modifier,
    state : TextFieldState,
    onHideLinkDialogClick : () -> Unit,
    onLinkClick : (String) -> Unit,
    context : Context
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
                    onClick = { onHideLinkDialogClick() }
                ) {
                    Text(stringResource(R.string.cancel))
                }
                TextButton(
                    onClick = {
                        val url = state.text.toString()
                        if (isValidAlbumUrl(url, context)) {
                            val albumId = getAlbumFromIdUrl(url)
                            if (!albumId.isNullOrEmpty()) {
                                onLinkClick(albumId)
                                onHideLinkDialogClick()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.invalid_album_url),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                ) {
                    Text("Ok")
                }
            }
        }
    }
}

private fun isValidAlbumUrl(url : String, context : Context) : Boolean {
    if (url.isBlank()) return false

    return try {
        val uri = URL(url).toURI()
        val host = uri.host ?: ""

        val isCorrectHost = host.contains("spotify.com")
        val isAlbumPath = uri.path?.startsWith("/album/") == true

        isCorrectHost && isAlbumPath
    } catch (e : Exception) {
        showToast(context, e.message.toString())
        false
    }
}

private fun getAlbumFromIdUrl(url : String) : String? {
    val regex = Regex("/album/([^/?]+)")
    return regex.find(url)?.groupValues?.getOrNull(1)
}

@Composable
fun CollectionScreenContent(
    modifier: Modifier = Modifier,
    type : AlbumType?,
    onAction : (CollectionTypeAction) -> Unit,
    padding : PaddingValues,
    listState: LazyListState,
    onSearchClick : () -> Unit,
    onShowSortDialog : () -> Unit,
    onDismissSortDialog : () -> Unit,
    isSortDialogVisible : Boolean,
    isSearchVisible : Boolean,
    searchQuery : String,
    onHideSearchClick : () -> Unit,
    clearTextField : () -> Unit,
    albums : List<AlbumEntity>,
    onAlbumClick : (String) -> Unit,
    isLoading : Boolean,
    gridState : LazyGridState
) {
    var sortType by rememberSaveable { mutableStateOf(ListSortType.LATEST) }
    var viewType by rememberSaveable { mutableStateOf(ListViewType.GRID) }
    var listType by rememberSaveable { mutableStateOf(ListType.ALBUM) }

    Box(modifier = Modifier.fillMaxSize()) {
        val headerModifier = Modifier.layout { measurable, constraints ->
            val width = constraints.maxWidth + 20.dp.roundToPx()
            val placeable = measurable.measure(constraints.copy(maxWidth = width))
            layout(placeable.width, placeable.height) {
                placeable.place(-10.dp.roundToPx(), 0)
            }
        }

        if (viewType == ListViewType.GRID) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = gridState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                item(key = "header", span = { GridItemSpan(maxLineSpan) }) {
                    CollectionHeader(
                        modifier = headerModifier,
                        onSearchClick = onSearchClick,
                        onAction = onAction,
                        type = type,
                        onShowSortDialog = onShowSortDialog,
                        clearTextField = clearTextField,
                        onHideSearchClick = onHideSearchClick,
                        searchQuery = searchQuery,
                        isSearchVisible = isSearchVisible,
                        onDismissSortDialog = onDismissSortDialog,
                        isSortDialogVisible = isSortDialogVisible,
                        onSortChange = { sortType = it },
                        onViewChange = { viewType = it },
                        onListTypeChange = { listType = it },
                        viewType = viewType
                    )
                }
                if (albums.isEmpty()) {
                    item(key = "empty_list", span = { GridItemSpan(maxLineSpan) }) {
                        EmptyList()
                    }
                }
                items(
                    items = albums,
                    key = { it.id },
                    contentType = { "album_item" }
                ) { item ->
                    AlbumGridCard(
                        item = item.toAlbum(),
                        onAlbumClick = onAlbumClick
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(),
                state = listState,
                contentPadding = PaddingValues(bottom = 10.dp, start = 10.dp, end = 10.dp)
            ) {
                item(key = "header") {
                    CollectionHeader(
                        modifier = headerModifier,
                        onSearchClick = onSearchClick,
                        onAction = onAction,
                        type = type,
                        onShowSortDialog = onShowSortDialog,
                        clearTextField = clearTextField,
                        onHideSearchClick = onHideSearchClick,
                        searchQuery = searchQuery,
                        isSearchVisible = isSearchVisible,
                        onDismissSortDialog = onDismissSortDialog,
                        isSortDialogVisible = isSortDialogVisible,
                        onSortChange = { sortType = it },
                        onViewChange = { viewType = it },
                        onListTypeChange = { listType = it },
                        viewType = viewType
                    )
                }
                if (albums.isEmpty()) {
                    item {
                        EmptyList()
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(
                    items = albums,
                    key = { it.id },
                    contentType = { "album_item" }
                ) { item ->
                    Box(modifier = Modifier.padding(bottom = 5.dp)) {
                        AlbumListRow(
                            item = item,
                            onAlbumClick = onAlbumClick
                        )
                    }
                }
            }
        }

        if (isLoading && albums.isEmpty()) {
            ContainedLoadingIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CollectionHeader(
    modifier: Modifier = Modifier,
    onAction: (CollectionTypeAction) -> Unit,
    isSearchVisible : Boolean,
    type : AlbumType?,
    onSortChange : (ListSortType) -> Unit,
    onViewChange : (ListViewType) -> Unit,
    onListTypeChange: (ListType) -> Unit,
    onSearchClick : () -> Unit,
    onShowSortDialog: () -> Unit,
    onDismissSortDialog: () -> Unit,
    isSortDialogVisible : Boolean,
    searchQuery: String,
    clearTextField: () -> Unit,
    onHideSearchClick : () -> Unit,
    viewType: ListViewType
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (chips, options, search) = createRefs()

        val startGuideline = createGuidelineFromStart(10.dp)
        val endGuideline = createGuidelineFromEnd(0.dp)

        val keyboardController = LocalSoftwareKeyboardController.current

        Box(modifier = Modifier.constrainAs(chips) {
            top.linkTo(parent.top, margin = 8.dp)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
        }) {
            AlbumTypeChipList(
                selectedChip = type,
                onChipSelected = { selectedType ->
                    onAction(CollectionTypeAction.OnTypeClick(selectedType))
                }
            )
        }

        Box(modifier = Modifier.constrainAs(options) {
            top.linkTo(chips.bottom, margin = 8.dp)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
        }) {
            ListOptions(
                onSortChange = onSortChange,
                onViewChange = onViewChange,
                onListTypeChange = onListTypeChange,
                onSearchClick = onSearchClick,
                isSortDialogVisible = isSortDialogVisible,
                onShowSortDialog = onShowSortDialog,
                onDismissSortDialog = onDismissSortDialog,
                viewType = viewType
            )
        }

        Box(modifier = Modifier.constrainAs(search){
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            top.linkTo(options.bottom, margin = 8.dp)
            width = Dimension.fillToConstraints
        }) {
            AnimatedVisibility(
                visible = isSearchVisible,
                enter = expandVertically(
                    expandFrom = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeIn(),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(300)
                ) + fadeOut()
            ) {
                SearchField(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    onValueChange = { query ->
                        onAction(CollectionTypeAction.OnQueryChange(query))
                    },
                    onSearch = { keyboardController?.hide() },
                    searchQuery = searchQuery,
                    onCloseClick = {
                        onHideSearchClick()
                        clearTextField()
                    }
                )
            }
        }
    }
}