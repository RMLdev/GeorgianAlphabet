package com.rmldev.georgianalphabet.core.datastore.api

sealed interface DataStoreCreationData {

    /**
     * Plain store. Included in Auto Backup and device transfer.
     */
    object Default : DataStoreCreationData

    /**
     * Store encrypted with a Tink key protected by a master key from AndroidKeyStore.
     *
     * The master key never leaves the device, so neither the keyset nor the encrypted
     * files are backed up (see `app/src/main/res/xml`). After a restore on a new device
     * the store starts from `defaultValue`. Keep data that must survive a device change
     * in a [Default] store.
     */
    data class Encrypted(
        val keysetName: String,
        val masterKeyName: String,
    ) : DataStoreCreationData
}
