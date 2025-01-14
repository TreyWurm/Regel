package at.nukular.core.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class BaseOkHttpClient

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class AdvancedOkHttpClient