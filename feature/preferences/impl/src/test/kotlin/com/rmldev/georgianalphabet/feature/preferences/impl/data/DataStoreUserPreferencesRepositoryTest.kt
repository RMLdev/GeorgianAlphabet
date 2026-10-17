package com.rmldev.georgianalphabet.feature.preferences.impl.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreCreationData
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreProvider
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import com.rmldev.georgianalphabet.feature.preferences.api.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.test.assertEquals

class DataStoreUserPreferencesRepositoryTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private val ruLocale = LocaleScriptResolver { Locale.forLanguageTag("ru-RU") }

    @Test
    fun `clean install returns defaults with script from locale`() = runTest {
        withRepository(ruLocale) { repository ->
            assertEquals(
                UserPreferences(onboardingCompleted = false, knownScript = KnownScript.CYRILLIC, userName = null),
                repository.observe().first(),
            )
        }
    }

    @Test
    fun `values survive restart`() = runTest {
        withRepository(ruLocale) { it.completeOnboarding(KnownScript.LATIN, "  Роман ") }

        withRepository(ruLocale) { repository ->
            assertEquals(
                UserPreferences(onboardingCompleted = true, knownScript = KnownScript.LATIN, userName = "Роман"),
                repository.observe().first(),
            )
        }
    }

    @Test
    fun `skip keeps locale script and blank name becomes null`() = runTest {
        withRepository(LocaleScriptResolver { Locale.US }) { repository ->
            repository.completeOnboarding(knownScript = null, userName = "   ")

            assertEquals(
                UserPreferences(onboardingCompleted = true, knownScript = KnownScript.LATIN, userName = null),
                repository.observe().first(),
            )
        }
    }

    @Test
    fun `setters update single fields`() = runTest {
        withRepository(ruLocale) { repository ->
            repository.setKnownScript(KnownScript.LATIN)
            repository.setUserName("Tamar")

            val prefs = repository.observe().first()
            assertEquals(KnownScript.LATIN, prefs.knownScript)
            assertEquals("Tamar", prefs.userName)
            assertEquals(false, prefs.onboardingCompleted)
        }
    }

    /** Каждый вызов — отдельный «запуск приложения» со своим стором поверх одного файла. */
    private suspend fun TestScope.withRepository(
        resolver: LocaleScriptResolver,
        block: suspend (DataStoreUserPreferencesRepository) -> Unit,
    ) {
        val job = Job()
        val provider = FileDataStoreProvider(tmp.root, CoroutineScope(UnconfinedTestDispatcher(testScheduler) + job))
        block(DataStoreUserPreferencesRepository(provider, resolver))
        job.cancelAndJoin()
    }

    private class FileDataStoreProvider(private val dir: File, private val scope: CoroutineScope) : DataStoreProvider {
        private val stores = mutableMapOf<String, DataStore<*>>()

        @Suppress("UNCHECKED_CAST")
        override suspend fun <T : Any> provide(
            entityClazz: KClass<T>,
            entitySerializer: Serializer<T>,
            dataStoreFileName: String,
            dataStoreCreationData: DataStoreCreationData,
        ): DataStore<T> = stores.getOrPut(dataStoreFileName) {
            DataStoreFactory.create(entitySerializer, scope = scope) { File(dir, dataStoreFileName) }
        } as DataStore<T>
    }
}
