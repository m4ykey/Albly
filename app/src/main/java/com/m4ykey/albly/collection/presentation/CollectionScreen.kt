package com.m4ykey.albly.collection.presentation

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.m4ykey.albly.R
import com.m4ykey.albly.app.ui.navigation.Screen
import com.m4ykey.albly.collection.presentation.drawer.DrawerItem
import com.m4ykey.albly.collection.presentation.type.album.AlbumType
import com.m4ykey.albly.collection.presentation.type.components.chip.ListTypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.SortTypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.TypeChip
import com.m4ykey.albly.collection.presentation.type.components.chip.ViewTypeChip
import com.m4ykey.albly.collection.presentation.type.list.ListSortType
import com.m4ykey.albly.collection.presentation.type.list.ListType
import com.m4ykey.albly.collection.presentation.type.list.ListViewType
import com.m4ykey.albly.search.presentation.SearchBarTextField
import com.m4ykey.core.chip.ChipItem
import com.m4ykey.core.ext.CenteredContent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    modifier : Modifier = Modifier,
    onSearch : () -> Unit,
    viewModel : CollectionViewModel = viewModel(),
    onLinkClick : (String) -> Unit,
    onNavigateTo : (String) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewState by viewModel.albumType.collectAsState()
    val type by rememberUpdatedState(viewState.type)

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
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .statusBarsPadding(),
                    title = { Text(text = stringResource(id = R.string.collection)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                contentDescription = "Menu",
                                painter = painterResource(R.drawable.ic_menu)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { onSearch() }
                        ) {
                            Icon(
                                contentDescription = stringResource(R.string.search),
                                painter = painterResource(R.drawable.ic_search)
                            )
                        }
                        IconButton(
                            onClick = {
                                viewModel.showLinkDialog()
                            }
                        ) {
                            Icon(
                                contentDescription = stringResource(R.string.enter_url),
                                painter = painterResource(R.drawable.ic_link)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            CollectionScreenContent(
                type = type,
                onAction = onAction,
                padding = padding,
                listState = listState,
                viewModel = viewModel
            )
        }
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
                                onClick = { viewModel.hideLinkDialog() }
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
                                            viewModel.hideLinkDialog()
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
        }
    }
}

private fun isValidAlbumUrl(url : String) : Boolean {
    try {
        val uri = URL(url).toURI()
        if (uri.host == "open.spotify.com" && uri.path.startsWith("/album/")) {
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
                painter = painterResource(R.drawable.ic_link)
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
                        painter = painterResource(R.drawable.ic_close),
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
    onAction : (CollectionTypeAction) -> Unit,
    padding : PaddingValues,
    listState: LazyListState,
    viewModel: CollectionViewModel
) {
    var sortType by rememberSaveable { mutableStateOf(ListSortType.LATEST) }
    var viewType by rememberSaveable { mutableStateOf(ListViewType.GRID) }
    var listType by rememberSaveable { mutableStateOf(ListType.ALBUM) }

    val isSearchVisible by viewModel.isSearchVisible.collectAsState()
    val isSortDialogVisible by viewModel.isSortDialogVisible.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

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
                    onSortChange = { sortType = it },
                    onViewChange = { viewType = it },
                    onListTypeChange = { listType = it },
                    onSearchClick = { viewModel.showSearch() },
                    isSortDialogVisible = isSortDialogVisible,
                    onShowSortDialog = { viewModel.showSortDialog() },
                    onDismissSortDialog = { viewModel.hideSortDialog() }
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
                        onSearch = {

                        },
                        searchQuery = searchQuery,
                        onCloseClick = {
                            viewModel.hideSearch()
                            viewModel.clearTextField()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    onValueChange : (String) -> Unit,
    onSearch: () -> Unit,
    searchQuery : String,
    onCloseClick : () -> Unit
) {
    Row(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBarTextField(
            searchQuery = searchQuery,
            onSearch = onSearch,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onCloseClick) {
            Icon(
                contentDescription = stringResource(R.string.clear),
                painter = painterResource(R.drawable.ic_close)
            )
        }
    }
}

@Composable
fun ListOptions(
    modifier: Modifier = Modifier,
    onSortChange : (ListSortType) -> Unit,
    onListTypeChange : (ListType) -> Unit,
    onViewChange : (ListViewType) -> Unit,
    onSearchClick : () -> Unit,
    isSortDialogVisible : Boolean,
    onShowSortDialog : () -> Unit,
    onDismissSortDialog : () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ListTypeChip(onChange = onListTypeChange)
        Spacer(modifier = Modifier.width(10.dp))
        SortTypeChip(
            onChange = onSortChange,
            isDialogVisible = isSortDialogVisible,
            onShowDialog = onShowSortDialog,
            onDismissDialog = onDismissSortDialog
        )
        Spacer(modifier = Modifier.width(10.dp))
        ViewTypeChip(onChange = onViewChange)
        Spacer(modifier = Modifier.weight(1f))
        TypeChip(
            onClick = onSearchClick,
            icon = painterResource(R.drawable.ic_search),
            label = null
        )
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