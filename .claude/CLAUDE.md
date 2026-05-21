# Project conventions

These rules apply to every change in this repo. Follow them silently — don't
ask whether they apply, just do.

## Communication

- Reply in **Russian** (the user writes in Russian).
- Never mention Claude/Anthropic in commit messages, code comments, PR
  descriptions or anywhere else. No `Co-Authored-By: Claude …` trailers, no
  "Generated with Claude Code" footers. Everything goes from the user's voice.

## Commits

- Style: `feat: …`, `fix: …`, `ref: …`, `chore: …` — lowercase after the
  colon, English summary.
- Atomic: one logical change per commit. Split mixed changes into separate
  commits before staging.
- Use HEREDOC for multi-line messages.
- Never use `--amend` to rewrite already pushed commits.
- Don't commit IDE / personal files: `.claude/settings.local.json`,
  `.idea/*`, OS metadata. Stage only what's relevant to the feature.

## Strings / localization

- **EVERY** user-visible string lives in
  `core/ui/src/main/res/values/strings*.xml`. Never hardcode text in
  composables. Use `stringResource(R.string.xxx)` or
  `pluralStringResource(R.plurals.xxx, count, count)`.
- Feature-specific groups can live in dedicated files in the same folder
  (e.g. `strings_questions.xml` for FAQ). Same `R.string.*` namespace.
- Plurals go in `<plurals>` elements, never compose strings by hand.
- When the user adds translatable strings, the supported set is:
  `ru, de, es, fr, hi, it, ja, ko, pt, ar, en` (translations go into
  `values-{lang}/strings.xml`).
- Strings with quotes / apostrophes — escape as `\'`, `\"`. Don't break
  Android XML.

## Colors

- All palette tokens live in
  `core/ui/src/main/java/dev/akmvxx/ui/AppColors.kt`.
- Use `AppColors.Xxx` everywhere — never inline `Color(0xFF…)` inside
  components. If a new shade is needed, add it to `AppColors` first.

## Module layout

```
:app                    — composition root, wires features and Hilt
:navigation             — NavKey types (RootNavKey, TabsNavKey), nav
                          helpers (rootNavigator, tabsNavigator)
:domain                 — entities, use cases, validators, repository
                          interfaces — pure Kotlin, no Android
:data                   — repository implementations, network, DB
:core:common            — pure Kotlin helpers (Result, Validator,
                          errors, Email/Link helpers, file utils)
:core:android           — Android-aware utilities: MVI base,
                          BaseNavigationViewModel, SnackbarManager,
                          Internet, Language
:core:ui                — design system: AppColors, strings, plurals,
                          drawables, reusable Compose components
:feature:splash         — splash screen
:feature:tabs           — TabsScreen container with bottom nav
:feature:browse         — catalog of mods
:feature:saved          — favorites list
:feature:propose        — propose / feedback form
:feature:help           — FAQ
:feature:mod            — single mod detail
```

**Hard rule:** features do not depend on each other. They only depend on
`:core:*`, `:navigation`, `:domain`. Cross-feature glue lives in `:app` or
in shared modules.

`:core:ui` exposes the Compose bundle via `api(…)` so features pick it up
transitively. `:core:android` exposes lifecycle + nav3 + compose runtime
via `api(…)` so VM bases work for downstream features.

## Architecture (MVI)

Every feature follows the same skeleton:

```
feature/X/src/main/java/dev/akmvxx/feature/X/
    XContract.kt        — data class XState, sealed XIntent, sealed XEvent
    XViewModel.kt       — @HiltViewModel extending MVI<XIntent, XState, XEvent>
    XScreen.kt          — @Composable wiring VM + state to XContent
    XEntry.kt           — fun EntryProviderScope<NavKey>.xEntry()
    ui/                 — internal feature-local composables
```

- `MVI<I, S, E>` base is in `:core:android`. State exposed as
  `state: StateFlow<S>`, send via `sendIntent(intent)`, override
  `handleIntent`.
- VM constructor inject `@ApplicationContext context: Context` when string
  resources are needed (for snackbar). Inject `SnackbarManager` from
  `core:android` for one-shot notifications.
- All errors map to `R.string.*` via `when (error)` over
  `DataError.Network` + per-feature `ValidatorError` enums. Show via
  `snackbarManager.showMessage(context.getString(resId))`. Never bubble
  raw error types or technical messages to the user.
- Screen-level lifecycle uses `ScreenUiState` (Loading / Success /
  Error(message)) from `:core:ui/entity`. Branch in the screen with
  exhaustive `when`.

### Screen template

```kotlin
@Composable
fun XScreen(viewModel: XViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    XContent(state = state, onIntent = viewModel::sendIntent)
}

@Composable
private fun XContent(state: XState, onIntent: (XIntent) -> Unit) { … }
```

Stateful wrapper (`XScreen`) gets the VM. Stateless `XContent` takes
state + intent dispatcher — easy preview/test.

## Navigation (Jetpack Navigation 3)

- Two scopes: **Root** (Splash → Tabs → ModDetail …) and **Tabs**
  (Browse / Saved / Propose / Help). Each has its own
  `BaseNavigationViewModel` subclass (`RootNavigator`, `TabsNavigator`)
  exposed by `rootNavigator()` / `tabsNavigator()` Composable helpers
  from `:navigation`.
- `NavKey` types are sealed interfaces in `:navigation`:
  `RootNavKey { Splash; Tabs; ModDetail(modId) }`,
  `TabsNavKey { Browse; Favorite; Propose; Help }`. Add new destinations
  here.
- Each feature contributes one entry to the back stack via an extension
  function: `fun EntryProviderScope<NavKey>.xEntry() { entry<XKey> { … } }`.
  These extensions are composed in `app/RootNavigation.kt` /
  `feature:tabs/TabsScreen.kt`.
- Navigate from anywhere by calling `rootNavigator()` / `tabsNavigator()`
  and using `push(key)`, `replace(key)`, `replaceAll(key)`, `pop()`,
  `pushOnTop(key)`. No callback prop-drilling.

## Components

- **Global, reusable** (button, text field, dropdown, tab row, etc.) go
  into `core/ui/src/main/java/dev/akmvxx/ui/components/` and are `public`.
  Make them flexible: optional leading/trailing icon, configurable
  placeholder (default empty), enabled state, keyboard options, shape, …
- **Feature-internal** composables go into
  `feature/X/src/main/java/dev/akmvxx/feature/X/ui/` and are `internal`.
  One component per file. File name matches the composable.
- Never put `@Composable fun XScreen()` content in one file. Break each
  visual block (header, card, row, section, dialog) into its own file
  under `ui/`.

## Hilt + KSP wiring per feature

```kotlin
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    // …
}
```

Use `hiltViewModel<XViewModel>()` from `androidx.hilt:hilt-navigation-compose`
to get the VM in a composable. Provide use cases via Hilt modules in
`app/src/main/java/dev/akmvxx/app/di/`.

## Gradle conventions

- All versions and libraries in `gradle/libs.versions.toml`. Reference
  via `libs.…`.
- Use bundles for groups of libs that move together (`libs.bundles.compose`).
- Use `api(…)` only when the type leaks into the module's public API
  (e.g. `BaseNavigationViewModel.backStack: SnapshotStateList<NavKey>` in
  `:core:android`). Otherwise `implementation(…)`.
- KSP, not kapt.
- Don't pin SDK / Java versions in feature build files — they inherit
  from the application module's defaults.

## Domain rules to respect

- `domain` is pure Kotlin, no Android imports. Helpers needed there
  (`String.isEmailValid()`, `String.isLink()`, etc.) live in
  `:core:common`.
- Validators return `Result<Unit, ValidatorError>`. Use cases compose
  validator + repository and return `Result<T, Error>`.

## When making big changes

- For non-trivial tasks ask short clarifying questions before coding
  (architecture choice, scope, where to put a new module/screen, etc.).
- After answers, lay out the work as a `TodoWrite` list, then execute
  in small commits.
