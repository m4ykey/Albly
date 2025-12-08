@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.m4ykey.core.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.delay

private val defaultLoading : @Composable () -> Unit = { DefaultLoading() }
private val defaultError : @Composable (Throwable?) -> Unit = { DefaultError(it) }
private val defaultEmpty : @Composable () -> Unit = { DefaultEmpty() }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun <T: Any> BasePagingList(
    modifier : Modifier = Modifier,
    items : LazyPagingItems<T>,
    loadingContent : @Composable () -> Unit = defaultLoading,
    errorContent : @Composable (Throwable?) -> Unit = defaultError,
    emptyContent : @Composable () -> Unit = defaultEmpty,
    listContent : @Composable (LazyPagingItems<T>) -> Unit,
    isActiveSearch : Boolean = true
) {
    var showLoading by remember { mutableStateOf(false) }

    val refreshState = items.loadState.refresh
    val isRefreshing = refreshState is LoadState.Loading

    if (!isActiveSearch) {
        emptyContent()
        return
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            showLoading = false
            delay(300L)
            showLoading = true
        } else {
            showLoading = false
        }
    }

    when (refreshState) {
        is LoadState.Loading -> {
            if (showLoading || items.itemCount == 0) loadingContent()
        }

        is LoadState.Error -> {
            errorContent(refreshState.error)
        }

        is LoadState.NotLoading -> {
            if (items.itemCount == 0) {
                emptyContent()
            } else {
                listContent(items)
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

    }
}

@Composable
private fun DefaultLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ContainedLoadingIndicator()
    }
}