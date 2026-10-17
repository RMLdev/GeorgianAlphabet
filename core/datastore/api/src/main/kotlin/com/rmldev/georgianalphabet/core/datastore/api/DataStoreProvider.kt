package com.rmldev.georgianalphabet.core.datastore.api

import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import kotlin.reflect.KClass

interface DataStoreProvider {

    suspend fun <T : Any> provide(
        entityClazz: KClass<T>,
        entitySerializer: Serializer<T>,
        dataStoreFileName: String,
        dataStoreCreationData: DataStoreCreationData = DataStoreCreationData.Default
    ): DataStore<T>
}