package com.m4ykey.collection.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.m4ykey.collection.R
import com.m4ykey.core.ext.ActionIconButton
import com.m4ykey.search.presentation.SearchBarTextField

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
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchBarTextField(
            searchQuery = searchQuery,
            onSearch = onSearch,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )
        ActionIconButton(
            onClick = onCloseClick,
            iconRes = R.drawable.ic_close,
            textRes = R.string.clear,
        )
    }
}