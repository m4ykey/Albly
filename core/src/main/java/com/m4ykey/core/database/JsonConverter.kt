package com.m4ykey.core.database

import kotlinx.serialization.json.Json

@PublishedApi
internal val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

inline fun <reified T> encode(value : T) : String {
    return json.encodeToString(value)
}

inline fun <reified T> decode(value : String) : T {
    return json.decodeFromString(value)
}