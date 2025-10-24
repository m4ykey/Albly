package com.m4ykey.albly.util.paging

import androidx.paging.PagingConfig

val pagingConfig = PagingConfig(
    pageSize = 20,
    enablePlaceholders = false,
    prefetchDistance = 5,
    initialLoadSize = 20
)