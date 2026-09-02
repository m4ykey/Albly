package com.m4ykey.core.ui

import kotlinx.coroutines.flow.MutableStateFlow

fun show(state : MutableStateFlow<Boolean>) {
    state.value = true
}
fun hide(state : MutableStateFlow<Boolean>) {
    state.value = false
}