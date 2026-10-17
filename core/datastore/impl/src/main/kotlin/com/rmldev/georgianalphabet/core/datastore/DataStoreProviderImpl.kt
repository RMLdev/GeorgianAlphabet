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
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

internal class DataStoreProviderImpl(
    private val dataStoreFileProducer: (String) -> File,
    private val aeadSerializerCreator: AeadSerializerCreator,
    private val scope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) : DataStoreProvider {

    private val dataStores = ConcurrentHashMap<String, CachedStore>()

    private val mutex = Mutex()

    override suspend fun <T : Any> provide(
        entityClazz: KClass<T>,
        entitySerializer: Serializer<T>,
        dataStoreFileName: String,
        dataStoreCreationData: DataStoreCreationData,
    ): DataStore<T> {
        val cached = dataStores[dataStoreFileName] ?: mutex.withLock {
            dataStores[dataStoreFileName] ?: run {
                val store = try {
                    createDataStore(entitySerializer, dataStoreFileName, dataStoreCreationData)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    throw IllegalStateException(
                        "Can't create DataStore<${entityClazz.simpleName}> with name $dataStoreFileName",
                        e
                    )
                }
                CachedStore(entityClazz, store).also { dataStores[dataStoreFileName] = it }
            }
        }

        return cached.cast(entityClazz, dataStoreFileName)
    }

    private class CachedStore(
        val entityClazz: KClass<*>,
        val store: DataStore<*>,
    ) {
        fun <T : Any> cast(requested: KClass<T>, fileName: String): DataStore<T> {
            require(entityClazz == requested) {
                "DataStore '$fileName' is already opened for ${entityClazz.simpleName}, " +
                    "can't open it for ${requested.simpleName}"
            }
            @Suppress("UNCHECKED_CAST")
            return store as DataStore<T>
        }
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
            corruptionHandler = ReplaceFileCorruptionHandler {
                Log.w(TAG, "DataStore '$dataStoreFileName' is corrupted, replaced with default value", it)
                entitySerializer.defaultValue
            },
        ) {
            dataStoreFileProducer(dataStoreFileName)
        }
    }

    private companion object {
        const val TAG = "DataStoreProviderImpl"
    }
}