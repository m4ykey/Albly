@file:OptIn(ExperimentalMaterial3Api::class)

package com.m4ykey.core.ext

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    @StringRes title : Int? = null,
    actions : @Composable RowScope.() -> Unit = {},
    navigation : @Composable () -> Unit = {},
    content : @Composable (PaddingValues) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    snackbarHostState: SnackbarHostState? = null
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { if (title != null) Text(text = stringResource(title)) },
                navigationIcon = {
                    navigation()
                },
                actions = {
                    actions()
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            snackbarHostState?.let { SnackbarHost(hostState = it) }
        }
    ) { padding ->
        content(padding)
    }
}