package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.allfonts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.feature.alphabet.ui.R

/**
 * «Все шрифты» буквы [letterId]. Заглушка до задачи 30: она заменит её экраном на своём сторе
 * по шаблону MviStore в этой же папке.
 */
@Suppress("UnusedParameter") // letterId: the store of task 30 loads the letter
@Composable
internal fun AllFontsRoute(
    letterId: Int,
    onBack: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    GeAlphIcons.ArrowBack,
                    contentDescription = stringResource(R.string.feature_alphabet_ui_all_fonts_back),
                )
            }
            Text(
                stringResource(R.string.feature_alphabet_ui_all_fonts_title),
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
