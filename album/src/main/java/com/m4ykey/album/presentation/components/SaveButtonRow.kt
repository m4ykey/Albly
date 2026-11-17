package com.m4ykey.album.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.m4ykey.album.R

@Composable
fun SaveButtonRow(
    modifier: Modifier = Modifier,
    isSaved : Boolean,
    isListenLaterSaved : Boolean,
    onSaveClick : () -> Unit,
    onListenLaterClick : () -> Unit,
    divider : Boolean = false
) {
    Row(
        modifier = modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedToggleIcon(
            checked = isSaved,
            checkedIcon = Icons.Default.Favorite,
            unCheckedIcon = Icons.Outlined.FavoriteBorder,
            onClick = onSaveClick,
            contentDescription = stringResource(R.string.save),
            checkedColor = Color(0xFFE53935)
        )
        if (divider) {
            Spacer(modifier = Modifier.width(10.dp))
        }
        AnimatedToggleIcon(
            checked = isListenLaterSaved,
            checkedIcon = Icons.Default.AccessTimeFilled,
            unCheckedIcon = Icons.Default.AccessTime,
            onClick = onListenLaterClick,
            contentDescription = stringResource(R.string.listen_later),
            checkedColor = MaterialTheme.colorScheme.primary,
            wiggle = true
        )
    }
}

@Composable
fun AnimatedToggleIcon(
    checked : Boolean,
    checkedIcon : ImageVector,
    unCheckedIcon : ImageVector,
    contentDescription : String,
    onClick : () -> Unit,
    wiggle : Boolean = false,
    checkedColor : Color
) {
   val scale = remember { Animatable(1f) }
   val rotation = remember { Animatable(0f) }
   var firstComposition by remember { mutableStateOf(true) }

    LaunchedEffect(checked) {
        if (firstComposition) {
            firstComposition = false
            return@LaunchedEffect
        }

        if (wiggle) {
            rotation.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 500
                    0f at 0
                    -18f at 80
                    16f at 190
                    -10f at 300
                    6f at 400
                    0f at 500
                }
            )
        } else {
            scale.snapTo(0.6f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    val tint by animateColorAsState(
        targetValue = if (checked) checkedColor else LocalContentColor.current,
        animationSpec = tween(300),
        label = "iconTint"
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier.then(Modifier.size(24.dp))
    ) {
        AnimatedContent(
            label = "iconSwap",
            targetState = checked,
            transitionSpec = { fadeIn(tween(150)) togetherWith fadeOut(tween(150)) }
        ) { isChecked ->
            Icon(
                tint = tint,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    rotationZ = rotation.value
                },
                contentDescription = contentDescription,
                imageVector = if (isChecked) checkedIcon else unCheckedIcon
            )
        }
    }
}