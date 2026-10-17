package com.rmldev.georgianalphabet.core.designsystem.font

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme

@Suppress("UnusedPrivateMember")
@Preview(heightDp = 1200)
@Composable
private fun FontCatalogPreview() {
    GeAlphTheme {
        Surface {
            Column(Modifier.padding(8.dp)) {
                FontCatalog.all.forEach { font ->
                    Text("${font.displayName} · ${font.difficulty}", style = MaterialTheme.typography.labelSmall)
                    Text(
                        text = "აბგდევზთიკლმნოპჟრსტუფქღყშჩცძწჭხჯჰ",
                        fontFamily = font.fontFamily,
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
}
