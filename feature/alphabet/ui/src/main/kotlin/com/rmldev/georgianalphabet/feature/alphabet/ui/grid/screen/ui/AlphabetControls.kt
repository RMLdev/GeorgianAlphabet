package com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rmldev.georgianalphabet.core.designsystem.icon.GeAlphIcons
import com.rmldev.georgianalphabet.core.designsystem.theme.GeAlphTheme
import com.rmldev.georgianalphabet.feature.alphabet.ui.R
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetFilter
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetFilterUi
import com.rmldev.georgianalphabet.feature.alphabet.ui.grid.screen.store.state.AlphabetSort
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val FILTER_COUNT_ALPHA = 0.72f

/**
 * Чипы «Все / Новые / Изучаю / Освоены» со счётчиками. Ряд прокручивается и выходит
 * за горизонтальные отступы сетки на [edgePadding], чтобы чипы доезжали до края экрана.
 */
@Composable
internal fun AlphabetFilterChips(
    filters: ImmutableList<AlphabetFilterUi>,
    selected: AlphabetFilter,
    onSelect: (AlphabetFilter) -> Unit,
    modifier: Modifier = Modifier,
    edgePadding: Dp = 16.dp,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .bleedHorizontally(edgePadding)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = edgePadding),
    ) {
        filters.forEach { item ->
            val isSelected = item.filter == selected
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(item.filter) },
                label = {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(stringResource(item.filter.labelRes))
                        Text(text = item.count.toString(), modifier = Modifier.alpha(FILTER_COUNT_ALPHA))
                    }
                },
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = GeAlphIcons.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize),
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}

/** Сегментированная кнопка «По алфавиту / По урокам» на всю ширину. */
@Composable
internal fun AlphabetSortSelector(
    selected: AlphabetSort,
    onSelect: (AlphabetSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sorts = AlphabetSort.entries
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        sorts.forEachIndexed { index, sort ->
            SegmentedButton(
                selected = sort == selected,
                onClick = { onSelect(sort) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = sorts.size),
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(sort.labelRes), maxLines = 1)
            }
        }
    }
}

/** Сообщение вместо пустой сетки, когда под фильтр не попала ни одна буква. */
@Composable
internal fun AlphabetFilterEmpty(filter: AlphabetFilter, modifier: Modifier = Modifier) {
    val textRes = filter.emptyRes ?: return
    Text(
        text = stringResource(textRes),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
    )
}

/** Растягивает элемент на [bleed] за пределы родителя с каждой стороны. */
private fun Modifier.bleedHorizontally(bleed: Dp): Modifier = layout { measurable, constraints ->
    val extra = if (constraints.hasBoundedWidth) bleed.roundToPx() * 2 else 0
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.minWidth + extra,
            maxWidth = constraints.maxWidth + extra,
        ),
    )
    layout(placeable.width - extra, placeable.height) {
        placeable.place(-extra / 2, 0)
    }
}

@get:StringRes
private val AlphabetFilter.labelRes: Int
    get() = when (this) {
        AlphabetFilter.ALL -> R.string.feature_alphabet_ui_filter_all
        AlphabetFilter.NEW -> R.string.feature_alphabet_ui_filter_new
        AlphabetFilter.LEARNING -> R.string.feature_alphabet_ui_filter_learning
        AlphabetFilter.MASTERED -> R.string.feature_alphabet_ui_filter_mastered
    }

// "All" is never empty once letters are loaded
@get:StringRes
private val AlphabetFilter.emptyRes: Int?
    get() = when (this) {
        AlphabetFilter.ALL -> null
        AlphabetFilter.NEW -> R.string.feature_alphabet_ui_filter_empty_new
        AlphabetFilter.LEARNING -> R.string.feature_alphabet_ui_filter_empty_learning
        AlphabetFilter.MASTERED -> R.string.feature_alphabet_ui_filter_empty_mastered
    }

@get:StringRes
private val AlphabetSort.labelRes: Int
    get() = when (this) {
        AlphabetSort.ALPHABET -> R.string.feature_alphabet_ui_sort_alphabet
        AlphabetSort.LESSONS -> R.string.feature_alphabet_ui_sort_lessons
    }

private val PreviewFilters = listOf(
    AlphabetFilterUi(AlphabetFilter.ALL, 33),
    AlphabetFilterUi(AlphabetFilter.NEW, 28),
    AlphabetFilterUi(AlphabetFilter.LEARNING, 4),
    AlphabetFilterUi(AlphabetFilter.MASTERED, 1),
).toImmutableList()

@Composable
private fun AlphabetControlsPreview(darkTheme: Boolean) {
    GeAlphTheme(darkTheme = darkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                AlphabetFilterChips(PreviewFilters, AlphabetFilter.NEW, onSelect = {})
                AlphabetSortSelector(AlphabetSort.LESSONS, onSelect = {})
                AlphabetFilterEmpty(AlphabetFilter.MASTERED)
            }
        }
    }
}

@Preview(name = "light", widthDp = 360)
@Composable
private fun AlphabetControlsLightPreview() = AlphabetControlsPreview(darkTheme = false)

@Preview(name = "dark", widthDp = 360)
@Composable
private fun AlphabetControlsDarkPreview() = AlphabetControlsPreview(darkTheme = true)
