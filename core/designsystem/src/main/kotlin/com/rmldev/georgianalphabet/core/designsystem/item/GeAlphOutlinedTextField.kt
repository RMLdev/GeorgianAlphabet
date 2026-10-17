package com.rmldev.georgianalphabet.core.designsystem.item

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent

@Composable
fun GeAlphOutlinedTextField(
    state: TextFieldState,
    labelText: String,
    placeholderText: String,
    modifier: Modifier = Modifier,
    isSecure: Boolean = false,
    /** Только для обычного поля: у secure-поля своя парольная клавиатура. */
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
) {
    var isFocused by remember { mutableStateOf(false) }

    if (isSecure) {
        OutlinedSecureTextField(
            state = state,
            labelPosition = TextFieldLabelPosition.Above(),
            label = {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (isFocused) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                    ),
                )
            },
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = GeAlphOutlinedTextFieldDefaults.colors(),
            textStyle = MaterialTheme.typography.bodyLarge,
            textObfuscationMode = TextObfuscationMode.RevealLastTyped,
            onKeyboardAction = onKeyboardAction,
            modifier = modifier
                .onFocusEvent({ isFocused = it.isFocused }),
        )
    } else {
        OutlinedTextField(
            state = state,
            labelPosition = TextFieldLabelPosition.Above(),
            label = {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = if (isFocused) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                    ),
                )
            },
            placeholder = {
                Text(
                    text = placeholderText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.outline,
                    ),
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = GeAlphOutlinedTextFieldDefaults.colors(),
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = modifier
                .onFocusEvent({ isFocused = it.isFocused }),
        )
    }
}

object GeAlphOutlinedTextFieldDefaults {
    @Composable
    fun colors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        errorBorderColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
        cursorColor = MaterialTheme.colorScheme.primary,
    )
}
