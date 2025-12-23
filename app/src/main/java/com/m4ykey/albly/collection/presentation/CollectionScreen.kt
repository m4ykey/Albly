@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.albly.collection.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.albly.R
import com.m4ykey.albly.album.data.local.model.AlbumEntity
import com.m4ykey.albly.app.ui.navigation.Screen
import com.m4ykey.albly.collection.presentation.components.AlbumGridCard
import com.m4ykey.albly.collection.presentation.components.AlbumListRow
import com.m4ykey.albly.collection.presentation.components.AlbumTypeChipList
import com.m4ykey.albly.collection.presentation.components.ListOptions
import com.m4ykey.albly.collection.presentation.components.SearchField
import com.m4ykey.albly.collection.presentation.components.UrlInputField
import com.m4ykey.albly.collection.presentation.drawer.DrawerItem
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.collection.presentation.type.list.ListSortType
import com.m4ykey.albly.collection.presentation.type.list.ListType
import com.m4ykey.albly.collection.presentation.type.list.ListViewType
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.core.ext.AppScaffold
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

@Composable
fun CollectionScreen(
    modifier : Modifier = Modifier,
    onSearch : () -> Unit,
    viewModel : CollectionViewModel = koinViewModel(),
    onLinkClick : (String) -> Unit,
    onNavigateTo : (String) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewState by viewModel.albumType.collectAsState()
    val type by rememberUpdatedState(viewState.type)

    val isSortDialogVisible by viewModel.isSortDialogVisible.collectAsState()
    val isSearchVisible by viewModel.isSearchVisible.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val albums by viewModel.albums.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val isDialogVisible by viewModel.isLinkDialogVisible.collectAsState()
    val state = rememberTextFieldState()

    val onAction = viewModel::onAction

    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(-1) }

    val context = LocalContext.current

    val items = listOf(
        DrawerItem(
            title = stringResource(R.string.new_release),
            route = Screen.NewReleaseScreen.screen,
            icon = R.drawable.ic_new_release
        ),
        DrawerItem(
            title = stringResource(R.string.listen_later),
            route = Screen.ListenLaterScreen.screen,
            icon = R.drawable.ic_clock
        )
    )

    LaunchedEffect(viewModel) {
        viewModel.loadAlbums()
    }

    LaunchedEffect(viewModel) {
        viewModel.collectionUiEvent.collectLatest { event ->
            when (event) {
                is CollectionUiEvent.ChangeType -> viewModel.updateType(event.type)
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
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            onNavigateTo(item.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        icon = {
                            Image(
                                painterResource(item.icon),
                                contentDescription = item.title,
                                colorFilter = ColorFilter.tint(color = LocalContentColor.current)
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
                    type = type,
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
                    onAlbumClick = {},
                    isLoading = isLoading
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
                        if (isValidAlbumUrl(url)) {
                            val albumId = getAlbumFromIdUrl(url)
                            if (!albumId.isNullOrEmpty()) {
                                onLinkClick(albumId)
                                onHideLinkDialogClick()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid album url",
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

private fun isValidAlbumUrl(url : String) : Boolean {
    try {
        val uri = URL(url).toURI()
        if (uri.host?.contains("spotify.com") == true && uri.path.startsWith("/album/")) {
            return true
        }
    } catch (e : MalformedURLException) {
        Log.i("CollectionScreen", "Error: ${e.message.toString()}")
    } catch (e : URISyntaxException) {
        Log.i("CollectionScreen", "Error: ${e.message.toString()}")
    }
    return false
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
    isLoading : Boolean
) {
    var sortType by rememberSaveable { mutableStateOf(ListSortType.LATEST) }
    var viewType by rememberSaveable { mutableStateOf(ListViewType.GRID) }
    var listType by rememberSaveable { mutableStateOf(ListType.ALBUM) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize(),
            state = listState
        ) {
            item(key = "header") {
                CollectionHeader(
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
                    onListTypeChange = { listType = it }
                )
            }
            items(
                items = albums,
                key = { it.id },
                contentType = { "album_item" }
            ) { item ->
                if (viewType == ListViewType.GRID) {
                    AlbumGridCard(
                        item = item,
                        onAlbumClick = onAlbumClick
                    )
                } else {
                    AlbumListRow(
                        item = item,
                        onAlbumClick = onAlbumClick
                    )
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
    onHideSearchClick : () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AlbumTypeChipList(
                selectedChip = type,
                onChipSelected = { selectedType ->
                    onAction(CollectionTypeAction.OnTypeClick(selectedType))
                }
            )
        }
        ListOptions(
            modifier = modifier.padding(horizontal = 5.dp),
            onSortChange = onSortChange,
            onViewChange = onViewChange,
            onListTypeChange = onListTypeChange,
            onSearchClick = onSearchClick,
            isSortDialogVisible = isSortDialogVisible,
            onShowSortDialog = onShowSortDialog,
            onDismissSortDialog = onDismissSortDialog
        )

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
                onValueChange = { query ->
                    onAction(CollectionTypeAction.OnQueryChange(query))
                },
                onSearch = {  },
                searchQuery = searchQuery,
                onCloseClick = {
                    onHideSearchClick()
                    clearTextField()
                }
            )
        }
    }
}