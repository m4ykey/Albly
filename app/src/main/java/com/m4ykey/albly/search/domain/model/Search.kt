package com.m4ykey.albly.search.domain.model

import com.m4ykey.albly.album.domain.model.Albums
import com.m4ykey.albly.artist.domain.model.Artists

data class Search(
    val albums : Albums,
    val artists : Artists
)
