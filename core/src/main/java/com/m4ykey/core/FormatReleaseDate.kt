package com.m4ykey.core

import kotlinx.datetime.LocalDate

fun formatReleaseDate(date : String?) : String? {
    return date?.let {
        try {
            if (it.length >= 10) {
                val date = LocalDate.parse(it)
                date.year.toString()
            } else if (it.length == 4) {
                it
            } else {
                null
            }
        } catch (e : Exception) {
            null
        }
    }
}