package com.m4ykey.core.chip

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.m4ykey.core.R

@Composable
fun ChipItem(
    modifier: Modifier = Modifier,
    label : String = "",
    selected : Boolean,
    onSelect : (Boolean) -> Unit,
    isLeadingIcon : Boolean = false,
    isTrailingIcon : Boolean = false
) {
    FilterChip(
        label = { Text(text = label) },
        onClick = { onSelect(!selected) },
        selected = selected,
        modifier = modifier.wrapContentWidth(),
        leadingIcon = {
            if (isLeadingIcon) {
                if (selected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = label,
                        modifier = modifier.size(14.dp)
                    )
                }
            }
        },
        trailingIcon = {
            if (isTrailingIcon) {
                if (selected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = label,
                        modifier = modifier.size(14.dp)
                    )
                }
            }
        }
    )
}