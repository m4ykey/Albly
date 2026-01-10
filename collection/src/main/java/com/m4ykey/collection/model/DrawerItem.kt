package com.m4ykey.collection.model

data class DrawerItem(
    val title : String,
    val onClick : () -> Unit,
    val icon : Int
)