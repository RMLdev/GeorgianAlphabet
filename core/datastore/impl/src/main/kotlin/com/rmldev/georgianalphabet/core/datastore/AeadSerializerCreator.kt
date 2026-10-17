package com.rmldev.georgianalphabet.core.datastore

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.core.Serializer
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.rmldev.georgianalphabet.core.datastore.api.DataStoreCreationData
import java.security.GeneralSecurityException
import java.security.KeyStore

internal interface AeadSerializerCreator {

    fun <T : Any> create(
        dataStoreCreationData: DataStoreCreationData.Encrypted,
        entitySerializer: Serializer<T>,
        dataStoreFileName: String,
    ): Serializer<T>
}

internal class AeadSerializerCreatorImpl(
    private val applicationContext: Context,
) : AeadSerializerCreator {

    override fun <T : Any> create(
        dataStoreCreationData: DataStoreCreationData.Encrypted,
        entitySerializer: Serializer<T>,
        dataStoreFileName: String
    ): Serializer<T> {
        val aeadSerializer = AeadSerializer(
            aead = LazyAead { createAead(dataStoreCreationData) },
            wrappedSerializer = entitySerializer,
            associatedData = dataStoreFileName.encodeToByteArray(),
        )

        return aeadSerializer
    }

    private fun createAead(dataStoreCreationData: DataStoreCreationData.Encrypted): Aead {
        AeadConfig.register()

        val handle = try {
            buildKeyset(dataStoreCreationData)
        } catch (_: GeneralSecurityException) {
            resetKeys(dataStoreCreationData)
            buildKeyset(dataStoreCreationData)
        } catch (_: IOException) {
            resetKeys(dataStoreCreationData)
            buildKeyset(dataStoreCreationData)
        }
        return handle.getPrimitive(
            RegistryConfiguration.get(),
            Aead::class.java
        )
    }

    private fun buildKeyset(
        dataStoreCreationData: DataStoreCreationData.Encrypted
    ): KeysetHandle =
        AndroidKeysetManager.Builder()
            .withSharedPref(
                applicationContext,
                dataStoreCreationData.keysetName,
                dataStoreCreationData.keysetPrefsFileName
            )
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri("android-keystore://${dataStoreCreationData.masterKeyName}")
            .build()
            .keysetHandle

    private fun resetKeys(dataStoreCreationData: DataStoreCreationData.Encrypted) {
        applicationContext.deleteSharedPreferences(dataStoreCreationData.keysetPrefsFileName)
        KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            .deleteEntry(dataStoreCreationData.masterKeyName)
    }

    private class LazyAead(create: () -> Aead) : Aead {
        private val delegate by lazy(create)

        override fun encrypt(plaintext: ByteArray, associatedData: ByteArray): ByteArray =
            delegate.encrypt(plaintext, associatedData)

        override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray): ByteArray =
            delegate.decrypt(ciphertext, associatedData)
    }
}