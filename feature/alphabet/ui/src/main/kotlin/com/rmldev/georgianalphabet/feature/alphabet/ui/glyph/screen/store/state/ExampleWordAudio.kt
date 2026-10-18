package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import com.rmldev.georgianalphabet.core.audio.api.AudioAssets
import com.rmldev.georgianalphabet.feature.alphabet.api.model.ExampleWord

/**
 * Путь звука слова в assets: явный [ExampleWord.audioAsset] или `audio/words/<slug>.mp3`,
 * где slug — латинская транслитерация без апострофов и пробелов (`khach'ap'uri` → `khachapuri`).
 * Тот же slug использует генерация звука (задача 21).
 */
internal fun ExampleWord.audioPath(): String =
    audioAsset ?: AudioAssets.word(transliterationLatin.lowercase().filter { it in 'a'..'z' })
