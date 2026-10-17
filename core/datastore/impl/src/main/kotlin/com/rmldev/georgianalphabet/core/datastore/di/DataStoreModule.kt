package com.rmldev.georgianalphabet.core.datastore.di

import android.content.Context
import androidx.datastore.dataStoreFile
import com.rmldev.georgianalphabet.core.coroutines.di.ApplicationScope
import com.rmldev.georgianalphabet.core.coroutines.di.Dispatcher
import com.rmldev.georgianalphabet.core.coroutines.di.GeAlphDispatchers
import com.rmldev.georgianalphabet.core.datastore.AeadSerializerCreator
import com.rmldev.georgianalphabet.core.datastore.AeadSerializerCreatorImpl
import com.rmldev.georgianalphabet.core.datastore.DataStoreProviderImpl
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun providesDataStoreProvider(
        @ApplicationContext applicationContext: Context,
        aeadSerializerCreator: AeadSerializerCreator,
        @ApplicationScope scope: CoroutineScope,
        @Dispatcher(GeAlphDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): DataStoreProvider = DataStoreProviderImpl(
        dataStoreFileProducer = { applicationContext.dataStoreFile(it) },
        aeadSerializerCreator = aeadSerializerCreator,
        scope = scope,
        ioDispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun providesAeadSerializerCreator(
        @ApplicationContext applicationContext: Context,
    ): AeadSerializerCreator = AeadSerializerCreatorImpl(applicationContext)
}