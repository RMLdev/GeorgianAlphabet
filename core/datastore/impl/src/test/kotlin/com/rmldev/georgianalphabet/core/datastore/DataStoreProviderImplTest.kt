package com.rmldev.georgianalphabet.core.datastore

import androidx.datastore.core.Serializer
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreCreationData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.InputStream
import java.io.OutputStream
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.fail

class DataStoreProviderImplTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private fun TestScope.provider() = DataStoreProviderImpl(
        dataStoreFileProducer = { tmp.root.resolve(it) },
        aeadSerializerCreator = object : AeadSerializerCreator {
            override fun <T : Any> create(
                dataStoreCreationData: DataStoreCreationData.Encrypted,
                entitySerializer: Serializer<T>,
                dataStoreFileName: String,
            ): Serializer<T> = fail("Encrypted stores are not used in this test")
        },
        scope = backgroundScope,
        ioDispatcher = StandardTestDispatcher(testScheduler),
    )

    @Test
    fun `same file name returns cached store`() = runTest {
        val provider = provider()

        val first = provider.provide(String::class, StringSerializer, "settings")
        val second = provider.provide(String::class, StringSerializer, "settings")

        assertSame(first, second)
    }

    @Test
    fun `different file names return different stores`() = runTest {
        val provider = provider()

        val first = provider.provide(String::class, StringSerializer, "a")
        val second = provider.provide(String::class, StringSerializer, "b")

        assertNotSame(first, second)
    }

    @Test
    fun `store reads and writes data`() = runTest {
        val store = provider().provide(String::class, StringSerializer, "settings")

        assertEquals("", store.data.first())
        store.updateData { "hello" }
        assertEquals("hello", store.data.first())
    }

    private object StringSerializer : Serializer<String> {
        override val defaultValue: String = ""
        override suspend fun readFrom(input: InputStream): String = input.readBytes().decodeToString()
        override suspend fun writeTo(t: String, output: OutputStream) = output.write(t.encodeToByteArray())
    }
}
