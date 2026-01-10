package com.m4ykey.collection.model

data class DrawerItem(
    val titleRes : Int,
    val onClick : () -> Unit,
    val icon : IconSource
)