package com.m4ykey.core.paging

import androidx.paging.PagingData
import androidx.paging.filter

fun <T: Any> PagingData<T>.distinctById(id : (T) -> Any?) : PagingData<T> {
    val seen = mutableSetOf<Any>()
    return filter { item -> id(item)?.let(seen::add) ?: true }
}