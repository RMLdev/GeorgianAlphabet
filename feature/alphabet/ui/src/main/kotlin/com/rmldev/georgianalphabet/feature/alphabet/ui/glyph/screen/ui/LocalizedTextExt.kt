package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText

/** Текст на языке интерфейса: как и строки модуля, английский только для английской локали, иначе русский. */
@Composable
@ReadOnlyComposable
internal fun LocalizedText.localized(): String = if (LocalConfiguration.current.locales[0].language == "en") en else ru
