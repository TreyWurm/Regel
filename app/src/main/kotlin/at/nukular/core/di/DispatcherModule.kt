package at.nukular.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {
    @DefaultDispatcher
    @Provides
    @Singleton
    fun provideDefaultDispatchers(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @IoDispatcher
    @Provides
    @Singleton
    fun providesIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @MainDispatcher
    @Provides
    @Singleton
    fun providesMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}