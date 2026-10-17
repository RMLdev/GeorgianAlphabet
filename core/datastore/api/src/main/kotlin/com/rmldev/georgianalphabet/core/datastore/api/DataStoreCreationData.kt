package com.rmldev.georgianalphabet.core.datastore.api

sealed interface DataStoreCreationData {

    object Default : DataStoreCreationData

    data class Encrypted(
        val keysetName: String,
        val keysetPrefsFileName: String,
        val masterKeyName: String,
    ) : DataStoreCreationData
}