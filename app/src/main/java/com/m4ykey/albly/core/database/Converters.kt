package com.m4ykey.albly.core.database

import kotlinx.serialization.json.Json

inline fun <reified T> encode(value : T) : String {
    return Json.encodeToString(value)
}

inline fun <reified T> decode(value : String) : T {
    return Json.decodeFromString(value)
}