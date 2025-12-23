package com.m4ykey.albly.collection.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.m4ykey.albly.R
import com.m4ykey.albly.collection.presentation.type.list.ListSortType
import com.m4ykey.albly.collection.presentation.type.list.ListType
import com.m4ykey.albly.collection.presentation.type.list.ListViewType
import com.m4ykey.albly.collection.presentation.type.list.label

@Composable
fun TypeChip(
    modifier: Modifier = Modifier,
    icon : Painter,
    label : String?,
    onClick : () -> Unit,
    iconSize : Dp = 18.dp
) {
    val shape = RoundedCornerShape(8.dp)

    val backgroundColor = Color.Transparent
    val contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = MaterialTheme.colorScheme.outlineVariant
    val borderWidth = 1.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(32.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = borderWidth,
                shape = shape,
                color = borderColor
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                modifier = modifier.size(iconSize),
                tint = contentColor
            )
            if (label != null) {
                Text(
                    text = label,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun TypeChipPrev() {
    TypeChip(
        onClick = {},
        label = "Latest Release",
        icon = painterResource(R.drawable.ic_grid)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortTypeChip(
    modifier: Modifier = Modifier,
    onChange : (ListSortType) -> Unit,
    onShowDialog : () -> Unit,
    onDismissDialog : () -> Unit,
    isDialogVisible : Boolean
) {
    var currentType by remember { mutableStateOf(ListSortType.LATEST) }

    TypeChip(
        onClick = onShowDialog,
        icon = painterResource(R.drawable.ic_arrow_sort),
        label = currentType.label(),
        iconSize = 16.dp
    )

    if (isDialogVisible) {
        AnimatedVisibility(
            visible = isDialogVisible,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            BasicAlertDialog(
                onDismissRequest = onDismissDialog,
                properties = DialogProperties(dismissOnClickOutside = true)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 6.dp,
                    modifier = modifier.padding(16.dp)
                ) {
                    Column(modifier = modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.sort_by),
                            modifier = modifier.padding(bottom = 8.dp),
                            fontSize = 20.sp
                        )

                        ListSortType.entries.forEach { type ->
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        currentType = type
                                        onChange(type)
                                        onDismissDialog()
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentType == type,
                                    onClick = {
                                        currentType = type
                                        onChange(type)
                                        onDismissDialog()
                                    }
                                )
                                Text(
                                    modifier = modifier.padding(start = 8.dp),
                                    text = type.label()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ViewTypeChip(
    onChange : (ListViewType) -> Unit,
    viewType: ListViewType
) {
    val nextType = when (viewType) {
        ListViewType.GRID -> ListViewType.LIST
        ListViewType.LIST -> ListViewType.GRID
    }

    val icon = when (viewType) {
        ListViewType.GRID -> painterResource(id = R.drawable.ic_grid)
        ListViewType.LIST -> painterResource(id = R.drawable.ic_list)
    }

    TypeChip(
        label = null,
        onClick = { onChange(nextType) },
        icon = icon
    )
}

@Composable
fun ListTypeChip(
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

    TypeChip(
        icon = icon,
        label = null,
        onClick = {
            currentType = nextType(currentType)
            onChange(currentType)
        }
    )
}