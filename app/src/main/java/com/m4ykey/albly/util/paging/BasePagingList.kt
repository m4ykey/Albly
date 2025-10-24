package com.m4ykey.albly.util.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.m4ykey.albly.util.CenteredContent

private val defaultLoading : @Composable () -> Unit = { DefaultLoading() }
private val defaultError : @Composable (Throwable?) -> Unit = { DefaultError(it) }
private val defaultEmpty : @Composable () -> Unit = { DefaultEmpty() }

@Composable
fun <T: Any> BasePagingList(
    modifier : Modifier = Modifier,
    listState : LazyListState,
    items : LazyPagingItems<T>,
    loadingContent : @Composable () -> Unit = defaultLoading,
    errorContent : @Composable (Throwable?) -> Unit = defaultError,
    contentPadding : PaddingValues = PaddingValues(10.dp),
    itemKey : (T) -> Any = { it.hashCode() },
    showDivider : Boolean = true,
    itemContent : @Composable (item : T) -> Unit,
    emptyContent : @Composable () -> Unit = defaultEmpty
) {
    when (val refreshState = items.loadState.refresh) {
        is LoadState.Loading -> {
            loadingContent()
        }

        is LoadState.Error -> {
            errorContent(refreshState.error)
        }

        is LoadState.NotLoading -> {
            if (items.itemCount == 0) {
                emptyContent()
            } else {
                CenteredContent(modifier = modifier.fillMaxSize()) { contentModifier ->
                    LazyColumn(
                        modifier = contentModifier,
                        state = listState,
                        contentPadding = contentPadding
                    ) {
                        items(
                            count = items.itemCount,
                            contentType = { "paged_item" },
                            key = { index -> items[index]?.let { itemKey(it) } ?: index }
                        ) { index ->
                            items[index]?.let { item ->
                                itemContent(item)

                                if (showDivider && index < items.itemCount - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 5.dp))
                                }
                            }
                        }

                        when (val appendState = items.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            is LoadState.NotLoading -> {}
                            is LoadState.Error -> {
                                item {
                                    errorContent(appendState.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultEmpty() {

}

@Composable
private fun DefaultError(error : Throwable?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = error?.message.toString())
    }
}

@Composable
private fun DefaultLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}