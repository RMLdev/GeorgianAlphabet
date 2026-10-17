package com.rmldev.georgianalphabet.core.datastore

import android.content.Context
import android.util.Log
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

class AeadSerializerCreatorImpl(
    val applicationContext: Context,
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
        } catch (e: GeneralSecurityException) {
            resetKeys(dataStoreCreationData, e)
            buildKeyset(dataStoreCreationData)
        } catch (e: IOException) {
            resetKeys(dataStoreCreationData, e)
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
                DataStoreFiles.KEYSET_PREFS_FILE_NAME
            )
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri("android-keystore://${dataStoreCreationData.masterKeyName}")
            .build()
            .keysetHandle

    // The keyset can't be decrypted (e.g. it was restored but the Keystore master key wasn't).
    // Old encrypted data is unreadable after this, the store starts from defaultValue.
    private fun resetKeys(dataStoreCreationData: DataStoreCreationData.Encrypted, cause: Exception) {
        Log.w(TAG, "Reset keyset '${dataStoreCreationData.keysetName}', encrypted data is lost", cause)

        applicationContext
            .getSharedPreferences(DataStoreFiles.KEYSET_PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(dataStoreCreationData.keysetName)
            .commit()
        KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
            .deleteEntry(dataStoreCreationData.masterKeyName)
    }

    private companion object {
        const val TAG = "AeadSerializerCreator"
    }

    private class LazyAead(create: () -> Aead) : Aead {
        private val delegate by lazy(create)

        override fun encrypt(plaintext: ByteArray, associatedData: ByteArray): ByteArray =
            delegate.encrypt(plaintext, associatedData)

        override fun decrypt(ciphertext: ByteArray, associatedData: ByteArray): ByteArray =
            delegate.decrypt(ciphertext, associatedData)
    }
}