package com.rmldev.georgianalphabet.core.datastore

/**
 * Paths referenced by the backup rules in `app/src/main/res/xml/backup_rules.xml`
 * and `data_extraction_rules.xml`. Update both files when changing them.
 */
internal object DataStoreFiles {

    /** SharedPreferences file with all Tink keysets (one key per `keysetName`). */
    const val KEYSET_PREFS_FILE_NAME = "core_datastore_keysets"

    /** Subdirectory of `files/datastore/` for encrypted stores. */
    const val ENCRYPTED_DIR = "encrypted"
}
