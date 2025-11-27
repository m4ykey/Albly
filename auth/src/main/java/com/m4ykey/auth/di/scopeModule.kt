package com.m4ykey.auth.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val scopeModule = module {
    single {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}