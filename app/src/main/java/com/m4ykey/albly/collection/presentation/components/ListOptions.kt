package com.m4ykey.albly.collection.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.m4ykey.albly.R
import com.m4ykey.albly.collection.presentation.type.list.ListSortType
import com.m4ykey.albly.collection.presentation.type.list.ListType
import com.m4ykey.albly.collection.presentation.type.list.ListViewType

@Composable
fun ListOptions(
    modifier: Modifier = Modifier,
    onSortChange : (ListSortType) -> Unit,
    onListTypeChange : (ListType) -> Unit,
    onViewChange : (ListViewType) -> Unit,
    onSearchClick : () -> Unit,
    isSortDialogVisible : Boolean,
    onShowSortDialog : () -> Unit,
    onDismissSortDialog : () -> Unit,
    viewType: ListViewType
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp),
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
        ViewTypeChip(
            onChange = onViewChange,
            viewType = viewType
        )
        Spacer(modifier = Modifier.weight(1f))
        TypeChip(
            onClick = onSearchClick,
            icon = painterResource(R.drawable.ic_search),
            label = null
        )
    }
}