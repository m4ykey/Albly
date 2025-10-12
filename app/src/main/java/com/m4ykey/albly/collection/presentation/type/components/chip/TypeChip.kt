package com.m4ykey.albly.collection.presentation.type.components.chip

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.m4ykey.albly.R
import com.m4ykey.albly.collection.presentation.type.list.ListSortType
import com.m4ykey.albly.collection.presentation.type.list.ListType
import com.m4ykey.albly.collection.presentation.type.list.ListViewType

@Composable
fun SortTypeChip(
    modifier: Modifier = Modifier,
    onChange : (ListSortType) -> Unit
) {
    var currentType by remember { mutableStateOf(ListSortType.LATEST) }
}

@Composable
fun ViewTypeChip(
    modifier: Modifier = Modifier,
    onChange : (ListViewType) -> Unit
) {
    var currentType by remember { mutableStateOf(ListViewType.GRID) }

    fun nextType(type : ListViewType) : ListViewType = when (type) {
        ListViewType.GRID -> ListViewType.LIST
        ListViewType.LIST -> ListViewType.GRID
    }

    val icon = when (currentType) {
        ListViewType.GRID -> painterResource(id = R.drawable.ic_grid)
        ListViewType.LIST -> painterResource(id = R.drawable.ic_list)
    }

    FilterChip(
        selected = false,
        label = {},
        onClick = {
            currentType = nextType(currentType)
            onChange(currentType)
        },
        leadingIcon = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        }
    )
}

@Composable
fun ListTypeChip(
    modifier : Modifier = Modifier,
    onChange : (ListType) -> Unit
) {
    var currentType by remember { mutableStateOf(ListType.ALBUM) }

    fun nextType(type : ListType) : ListType = when (type) {
        ListType.ALBUM -> ListType.ARTIST
        ListType.ARTIST -> ListType.ALBUM
    }

    val icon = when (currentType) {
        ListType.ALBUM -> painterResource(id = R.drawable.ic_album)
        ListType.ARTIST -> painterResource(id = R.drawable.ic_artist)
    }

    FilterChip(
        selected = false,
        label = {  },
        onClick = {
            currentType = nextType(currentType)
            onChange(currentType)
        },
        leadingIcon = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        }
    )
}