package com.m4ykey.albly.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthBaseUrl

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SpotBaseUrl