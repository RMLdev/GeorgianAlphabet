package com.rmldev.georgianalphabet.feature.preferences.impl.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

internal object StoredUserPreferencesSerializer : Serializer<StoredUserPreferences> {

    private val json = Json { ignoreUnknownKeys = true }

    override val defaultValue: StoredUserPreferences = StoredUserPreferences()

    override suspend fun readFrom(input: InputStream): StoredUserPreferences = try {
        json.decodeFromString(StoredUserPreferences.serializer(), input.readBytes().decodeToString())
    } catch (e: SerializationException) {
        throw CorruptionException("Can't read user preferences", e)
    } catch (e: IllegalArgumentException) {
        throw CorruptionException("Can't read user preferences", e)
    }

    override suspend fun writeTo(t: StoredUserPreferences, output: OutputStream) {
        output.write(json.encodeToString(StoredUserPreferences.serializer(), t).encodeToByteArray())
    }
}
