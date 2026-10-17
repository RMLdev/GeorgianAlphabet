package com.rmldev.georgianalphabet.core.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreCreationData
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.reflect.KClass

internal class DataStoreProviderImpl(
    private val dataStoreFileProducer: (String) -> File,
    private val aeadSerializerCreator: AeadSerializerCreator,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) : DataStoreProvider {

    private val dataStores: HashMap<String, DataStore<*>> = hashMapOf()

    private val mutex = Mutex()

    override suspend fun <T : Any> provide(
        entityClazz: KClass<T>,
        entitySerializer: Serializer<T>,
        dataStoreFileName: String,
        dataStoreCreationData: DataStoreCreationData,
    ): DataStore<T> {
        val dataStore = if (getStore<T>(dataStoreFileName) == null) {
            mutex.withLock {
                if (getStore<T>(dataStoreFileName) == null) {
                    val store = runCatching {
                        createDataStore(
                            entitySerializer,
                            dataStoreFileName,
                            dataStoreCreationData,
                        )
                    }.onFailure {
                        if (it is CancellationException) throw it

                        Log.e(
                            TAG,
                            "Can't retrieve DataStore<${entityClazz.simpleName}> with name $dataStoreFileName",
                            it
                        )
                    }.getOrNull()

                    store?.let { dataStores[dataStoreFileName] = it }
                }
                getStore(dataStoreFileName)
            }
        } else {
            getStore<T>(dataStoreFileName)
        }

        return requireNotNull(dataStore) {
            "Can't retrieve DataStore<${entityClazz.simpleName}> with name $dataStoreFileName"
        }
    }

    private fun <T : Any> getStore(fileName: String): DataStore<T>? {
        return dataStores[fileName] as DataStore<T>?
    }

    private fun <T : Any> createDataStore(
        entitySerializer: Serializer<T>,
        dataStoreFileName: String,
        dataStoreCreationData: DataStoreCreationData,
    ): DataStore<T> {
        val serializer = when (dataStoreCreationData) {
            is DataStoreCreationData.Default -> entitySerializer
            is DataStoreCreationData.Encrypted -> aeadSerializerCreator.create(
                dataStoreCreationData,
                entitySerializer,
                dataStoreFileName
            )
        }

        return DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(scope.coroutineContext + ioDispatcher),
            corruptionHandler = ReplaceFileCorruptionHandler { entitySerializer.defaultValue },
        ) {
            dataStoreFileProducer(dataStoreFileName)
        }
    }

    private companion object {
        const val TAG = "DataStoreProviderImpl"
    }
}