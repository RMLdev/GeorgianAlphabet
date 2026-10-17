package com.rmldev.georgianalphabet.feature.alphabet.ui.glyph.screen.store.state

import androidx.compose.runtime.Stable
import com.rmldev.georgianalphabet.core.designsystem.font.GeorgianFont
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LearningLevel
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText
import com.rmldev.georgianalphabet.feature.preferences.api.model.KnownScript
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Карточка буквы: по под-состоянию на каждую секцию экрана. */
@Stable
internal data class GlyphDetailsUiState(
    val letterId: Int = 0,
    /** Сам глиф буквы: сцена, тулбар «Буква ა». Пустой, пока буква не загружена. */
    val symbol: String = "",
    /** Загрузка завершена, но буквы с таким id нет: экран показывает только тулбар. */
    val isNotFound: Boolean = false,
    val stage: GlyphStageUi = GlyphStageUi(),
    val header: LetterHeaderUi = LetterHeaderUi(),
    val mastery: MasteryUi = MasteryUi(),
    /** null — блок ассоциации скрыт. Язык текста выбирает UI. */
    val association: LocalizedText? = null,
    /** Пустой — блок «Примеры слов» скрыт. */
    val words: ImmutableList<ExampleWordUi> = persistentListOf(),
    val similar: SimilarLettersUi = SimilarLettersUi(),
)

/** Сцена глифа: страницы открытых шрифтов и последняя страница «Ещё N шрифтов». */
@Stable
internal data class GlyphStageUi(
    /** Открытые шрифты в порядке каталога. */
    val fonts: ImmutableList<GeorgianFont> = persistentListOf(),
    /** Индекс текущего шрифта в [fonts]. */
    val currentFontIndex: Int = 0,
    /** Закрытые шрифты; 0 — страницы «Ещё N шрифтов» нет. */
    val lockedFontsCount: Int = 0,
)

/** Название, чтение и звук буквы. */
@Stable
internal data class LetterHeaderUi(
    val nameGeorgian: String = "",
    /** Имя буквы на знакомом алфавите: «ани» или «ani». */
    val name: String = "",
    /** Звук на знакомом алфавите для чипа «кириллица а» / «латиница a». */
    val sound: String = "",
    val knownScript: KnownScript = KnownScript.CYRILLIC,
    /** МФА без скобок: «ɑ». */
    val ipa: String = "",
    val archaic: Boolean = false,
    /** Путь звука названия в assets; файла может не быть. */
    val nameAudio: String = "",
    val isNamePlaying: Boolean = false,
)

/** Блок «Освоение»: уровень, «N из M шрифтов узнаёшь», точность и ячейки шрифтов. */
@Stable
internal data class MasteryUi(
    val level: LearningLevel = LearningLevel.NEW,
    /** Шрифты каталога, в которых букву узнают. */
    val recognizedCount: Int = 0,
    /** Все шрифты каталога, включая закрытые. */
    val fontsCount: Int = 0,
    /** Точность ответов в процентах; null — ответов ещё не было. */
    val accuracyPercent: Int? = null,
    /** Ячейки всех шрифтов каталога по порядку. */
    val cells: ImmutableList<FontCellUi> = persistentListOf(),
)

@Stable
internal data class FontCellUi(val font: GeorgianFont, val status: FontCellStatus)

internal enum class FontCellStatus {
    /** Букву в этом шрифте узнают. */
    RECOGNIZED,

    /** Шрифт открыт, но букву в нём ещё не узнают. */
    LEARNING,
    LOCKED,
}

/** Пример слова. */
@Stable
internal data class ExampleWordUi(
    /** Слово мхедрули целиком: ключ строки и аргумент `PlayWord`. */
    val georgian: String,
    /** Слово по кускам, куски из текущей буквы подсвечиваются. */
    val parts: ImmutableList<WordPart>,
    /** Транслит на знакомом алфавите. */
    val transliteration: String,
    val translation: LocalizedText,
    /** Путь звука слова в assets; null — звука нет. */
    val audio: String?,
    val isPlaying: Boolean,
)

@Stable
internal data class WordPart(val text: String, val highlighted: Boolean)

/** «Похожие буквы»: пустая строка скрывается, обе пустые — блок скрыт. */
@Stable
internal data class SimilarLettersUi(
    val byShape: ImmutableList<SimilarLetterUi> = persistentListOf(),
    val bySound: ImmutableList<SimilarLetterUi> = persistentListOf(),
)

/** Чип похожей буквы: глиф и имя на знакомом алфавите. */
@Stable
internal data class SimilarLetterUi(val id: Int, val symbol: String, val name: String)
