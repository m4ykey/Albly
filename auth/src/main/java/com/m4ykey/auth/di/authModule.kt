package com.m4ykey.auth.di

import org.koin.dsl.module

val authModule = module {
    includes(authNetworkModule, dataStoreModule, spotifyApiModule, scopeModule)
}