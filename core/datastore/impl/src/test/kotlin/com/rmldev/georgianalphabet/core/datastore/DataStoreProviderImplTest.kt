package com.rmldev.georgianalphabet.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreCreationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
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

    @Test
    fun `concurrent provide creates a single store`() = runTest {
        val provider = provider()

        val stores = (1..50).map {
            async(Dispatchers.Default) { provider.provide(String::class, StringSerializer, "settings") }
        }.awaitAll()

        assertEquals(1, stores.toSet().size)
    }

    @Test
    fun `same file with another type fails with clear message`() = runTest {
        val provider = provider()
        provider.provide(String::class, StringSerializer, "settings")

        val error = assertFailsWith<IllegalArgumentException> {
            provider.provide(Int::class, IntSerializer, "settings")
        }
        assertTrue("String" in error.message.orEmpty() && "Int" in error.message.orEmpty(), error.message)
    }

    @Test
    fun `corrupted file is replaced with default value`() = runTest {
        tmp.root.resolve("number").writeText("not a number")

        val store = provider().provide(Int::class, IntSerializer, "number")

        assertEquals(0, store.data.first())
    }

    private object IntSerializer : Serializer<Int> {
        override val defaultValue: Int = 0
        override suspend fun readFrom(input: InputStream): Int =
            input.readBytes().decodeToString().toIntOrNull()
                ?: throw CorruptionException("Not an int")
        override suspend fun writeTo(t: Int, output: OutputStream) = output.write(t.toString().encodeToByteArray())
    }

    private object StringSerializer : Serializer<String> {
        override val defaultValue: String = ""
        override suspend fun readFrom(input: InputStream): String = input.readBytes().decodeToString()
        override suspend fun writeTo(t: String, output: OutputStream) = output.write(t.encodeToByteArray())
    }
}
