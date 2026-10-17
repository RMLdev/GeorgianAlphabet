# Georgian Alphabet

Android-приложение для изучения грузинского алфавита (33 буквы мхедрули). Главная идея: научить узнавать буквы **в любом шрифте**, как на настоящих вывесках, в меню и книгах. Прогресс по букве учитывает, в скольких шрифтах её уверенно узнают, а новые шрифты открываются постепенно как награда.

Приложение работает офлайн, аккаунт не нужен. Интерфейс на русском и английском.

> Статус: ранняя разработка. Сейчас есть каркас приложения (многомодульная сборка, навигация, MVI, дизайн-система) и первая фича `alphabet` с экранами сетки алфавита и карточки буквы.

## Что будет в приложении

**MVP (v1.0)**
- Справочник алфавита и полноэкранная карточка буквы: свайп меняет шрифт; название, звук, транслитерация, ассоциация, примеры слов.
- Режимы: «буква → название» (4 варианта), «транслит/звук → буква», набор слова на время, «Пары».
- Форматы сессии: урок и на время.
- Карта уроков, освоение букв, коллекция шрифтов.
- Серия дней, дневная цель, локальные напоминания.
- Реклама AdMob и разовая покупка «Без рекламы».

**Позже (v1.1+ и v2):** аудио-диктант, «Найди лишнее», «Выживание», интервальное повторение, виджет «Буква дня», достижения, вывески и меню, прописи, заглавные мтаврули, схемы артикуляции. Архаичные буквы (ჱ ჲ ჳ ჴ ჵ) живут в отдельном справочном блоке.

## Стек

| Область | Технологии |
|---|---|
| Язык и UI | Kotlin 2.4, Jetpack Compose (Material 3, adaptive) |
| Архитектура | многомодульность, MVI на базе `:core:ui` |
| DI | Hilt |
| Навигация | Navigation 3, бэкстеки сохраняются через kotlinx.serialization |
| Хранение | DataStore (в т.ч. зашифрованный через Tink), Room (подключается с первой фичей с БД) |
| Асинхронность | Kotlin Coroutines / Flow |
| Сеть | OkHttp + Retrofit (каркас, в MVP приложение офлайн) |
| Сборка | Gradle 9.6 (Kotlin DSL), AGP 9.4, version catalog, convention-плагины, configuration cache |

Параметры Android (`build-logic/convention/.../AndroidProjectConfig.kt`): `minSdk 26`, `compileSdk 37.1`, `targetSdk 37`, `applicationId com.rmldev.georgianalphabet`. Debug-сборка ставится рядом с релизной (`.debug`, версия `-dev`).

## Сборка и запуск

Нужны Android Studio последней версии и Android SDK 37. JDK для демона Gradle (toolchain 25) скачивается автоматически через foojay.

```bash
./gradlew assembleDebug                         # собрать debug APK
./gradlew installDebug                          # установить на устройство/эмулятор
./gradlew testDebugUnitTest                     # unit-тесты
./gradlew :feature:alphabet:ui:compileDebugKotlin   # быстрая проверка одного модуля
```

Или откройте папку проекта в Android Studio и запустите конфигурацию `app`.

## Структура проекта

```
app/                    точка сборки: MainActivity, AppRoot, табы нижней навигации
core/
  ui/                   база MVI: StoreViewModel, MviActor, MviReducer
  designsystem/         тема, цвета, типографика, иконки, общие компоненты, шрифт Noto Serif Georgian
  navigation/api|impl   ключи навигации (NavigationKey, TabKey, OverlayScreenKey), Navigator, бэкстеки
  coroutines/           диспетчеры и @ApplicationScope
  datastore/api|impl    DataStoreProvider, в т.ч. зашифрованные сторы
feature/
  alphabet/api|impl|ui  справочник алфавита: сетка букв и карточка буквы
build-logic/
  convention/           convention-плагины (application, library, compose, hilt, feature api/impl/ui…)
  settings/             IncludeSubdirsAsModulesPlugin
gradle/libs.versions.toml   все версии зависимостей
```

Модули подключаются автоматически: плагин `IncludeSubdirsAsModulesPlugin` включает в сборку каждую папку внутри `core/` и `feature/`, где есть `build.gradle.kts`. Править `settings.gradle.kts` при добавлении модуля не нужно.

### Слои фичи

Каждая фича `feature/<name>/` состоит из модулей:

- **api** — контракт: интерфейсы UseCase, доменные модели, публичные NavKey. Чистый Kotlin.
- **impl** — репозитории и реализации UseCase, Hilt-модуль с `@Binds`.
- **ui** — экраны на Compose и MVI-стор на каждый экран. Зависит только от `api`.
- **db** — Room `@Entity` и `@Dao` фичи (пока ни в одной фиче не создан). Единая `RoomDatabase` собирается в `:app`.

Межфичевые зависимости идут только на `:api` другой фичи; `:app` единственный видит все слои и собирает граф.

### MVI

```
UI ─Intent─► ViewModel ─► Action ─► Actor (UseCase-ы) ─Event─► Reducer ─► State ─► UiState ─► UI
                                       └─ SideEffect (навигация, тосты) ─► UI
```

Файлы экрана генерируются IDE-шаблоном **MviStore** (File → New → MviStore, лежит в `.idea/fileTemplates`). Образец: `feature/alphabet/ui/.../grid/`.

Граф модулей, правила зависимостей и короткий гайд «как добавить экран или фичу» — в [docs/architecture.md](docs/architecture.md). Подробные чеклисты (регистрация NavKey, схема единой БД, соглашения по коду) — в [CLAUDE.md](CLAUDE.md), это же руководство используют AI-агенты.

## Соглашения

- Видимость по умолчанию `internal`; `public` только контракт модуля.
- Версии зависимостей только в `gradle/libs.versions.toml`, общая конфигурация в convention-плагинах.
- Строки UI в `strings.xml` модуля с префиксом модуля (например, `feature_alphabet_ui_screen_name`), без литералов в коде.
- Цвета, типографика и формы берутся из `:core:designsystem`.
- Диспетчеры инжектятся через `@Dispatcher(...)`, без прямых `Dispatchers.*`.
- `@SerialName` на каждом NavKey обязателен: имя попадает в сохранённое состояние.

## Шрифты и лицензии

Исходные файлы шрифтов лежат вне репозитория, в соседней папке `../fonts`. В приложение попадают только шрифты с проверенной лицензией: 13 разрешённых (OFL, Bitstream Vera, CC BY, GPL с font exception, public domain и др.) и 2 под AGPL-3.0 (Aznauri, XI20) с приложенным текстом лицензии. Royana, Cartoon 1471 и Garamond Typoart не включаются, пока их использование не согласовано с авторами.

Текст лицензии каждого встроенного шрифта кладётся рядом с ним (пример: `core/designsystem/NOTO_SERIF_GEORGIAN_OFL.txt`), а в приложении будет экран «Шрифты и лицензии» со списком авторов.

## Аудио

Для прототипа звуки и названия букв синтезируются Azure TTS (грузинские голоса ka-GE); к релизу планируется запись носителя.
