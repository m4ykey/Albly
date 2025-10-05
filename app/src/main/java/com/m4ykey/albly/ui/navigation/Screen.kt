package com.m4ykey.albly.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey, BottomNavItem {
    abstract override val icon : ImageVector
    abstract override val title : String
}

@Serializable
data object Collection : Screen() {
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Outlined.ListAlt
    override val title: String
        get() = "Collection"
}

@Serializable
data object News : Screen() {
    override val icon: ImageVector
        get() = Icons.Default.Public
    override val title: String
        get() = "News"
}