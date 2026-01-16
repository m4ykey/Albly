package com.m4ykey.auth.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

val scopeModule = module {
    single(named("ApplicationScope")) {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    single(named("IOScope")) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}