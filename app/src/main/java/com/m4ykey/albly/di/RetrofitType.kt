package com.m4ykey.albly.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SearchRetrofit