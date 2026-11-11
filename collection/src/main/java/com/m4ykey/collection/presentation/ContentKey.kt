package com.m4ykey.collection.presentation

import com.m4ykey.collection.presentation.type.ListType
import com.m4ykey.core.Sort

data class ContentKey(
    val query : String,
    val sort : Sort?,
    val type : ListType
)
