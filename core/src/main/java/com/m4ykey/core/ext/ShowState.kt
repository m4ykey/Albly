package com.m4ykey.core.ext

import kotlinx.coroutines.flow.MutableStateFlow

fun show(state : MutableStateFlow<Boolean>) {
    state.value = true
}
fun hide(state : MutableStateFlow<Boolean>) {
    state.value = false
}