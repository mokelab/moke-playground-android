# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A playground for experimenting with Android APIs (Compose, Material3, and friends). Each experiment lives in its own Android library module under `feature/**` and is surfaced as a menu entry ("feature") of the external **mokelab DevPortal** library.

## Commands

```bash
./gradlew assembleDebug                 # build
./gradlew installDebug                  # install on a device
./gradlew lint                          # Android Lint

./gradlew test                          # unit tests for every module
./gradlew :feature:m3:calendar:testDebugUnitTest          # a single module
./gradlew :app:testDebugUnitTest --tests "com.mokelab.playground.android.ExampleUnitTest"  # a single test

./gradlew connectedDebugAndroidTest     # instrumented tests (device/emulator required)
```

Always declare dependency versions in the version catalog (`gradle/libs.versions.toml`). DevPortal is resolved from its own Maven repository, `https://mokelab.github.io/android-devportal/repo`, already declared in `settings.gradle.kts`.

## Build logic (convention plugins)

Every module's `android {}` build settings live in convention plugins under `build-logic/`, an included build wired up by `includeBuild("build-logic")` inside `pluginManagement {}` in `settings.gradle.kts`. Modules apply them by alias from the catalog:

| plugin id | catalog alias | what it does |
|---|---|---|
| `mokelab.android.application` | `libs.plugins.mokelab.android.application` | `com.android.application` + common settings + `targetSdk = 36` |
| `mokelab.android.application.compose` | `libs.plugins.mokelab.android.application.compose` | above + Compose |
| `mokelab.android.library` | `libs.plugins.mokelab.android.library` | `com.android.library` + common settings |
| `mokelab.android.library.compose` | `libs.plugins.mokelab.android.library.compose` | above + Compose |
| `mokelab.hilt` | `libs.plugins.mokelab.hilt` | applies KSP + the Hilt Gradle plugin |

Common settings = `compileSdk 36.1` / `minSdk 24` / `targetSdk 36` / `testInstrumentationRunner` / Java 17, applied from `build-logic/convention/src/main/kotlin/com/mokelab/playground/KotlinAndroid.kt`. The SDK and Java levels themselves are constants in `AndroidConfig.kt` next to it — change them there, not in a module.

The `.compose` variants additionally add the Compose base dependencies (`AndroidCompose.kt`), so a module applying them must **not** declare these itself:

```
implementation(platform(libs.androidx.compose.bom))    androidTestImplementation(platform(...))
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.ui.graphics)
implementation(libs.androidx.compose.ui.tooling.preview)
implementation(libs.androidx.compose.material3)
debugImplementation(libs.androidx.compose.ui.tooling)          // @Preview rendering
debugImplementation(libs.androidx.compose.ui.test.manifest)
androidTestImplementation(libs.androidx.compose.ui.test.junit4)
```

Anything beyond that base — `material-icons`, `activity-compose` — stays in the module's own `dependencies {}`. Apart from those Compose lines the convention plugins configure only `android {}`: Hilt's `hilt.android` / `ksp(hilt.compiler)` and module-local file references (`consumerProguardFiles`, `buildTypes { release { proguardFiles(...) } }`) all stay in the module.

`build-logic/convention/build.gradle.kts` declares the AGP/Kotlin/KSP/Hilt Gradle plugins as `compileOnly`; the actual implementations reach the buildscript classpath through the `alias(...) apply false` lines in the root `build.gradle.kts`, so those must stay.

## Architecture

**There is no MainActivity.** The launcher activity is `com.mokelab.android.devportal.DevPortalActivity`, provided by the DevPortal library and declared in `app/src/main/AndroidManifest.xml`. The app module only owns `PlaygroundApplication` (`@HiltAndroidApp`); every screen lives in a feature module.

Each feature module contributes a `DevPortalFeature` from its `Module.kt` via Hilt's `@IntoSet`. DevPortal collects that set to build its menu and renders each screen as a navigation3 entry.

For a feature with a **single screen** — the common case — use `singleScreenFeature()` from `:core:devportal` and the whole `Module.kt` is this (see `feature/m3/calendar/src/main/java/.../Module.kt`):

```kotlin
@Module
@InstallIn(ActivityRetainedComponent::class)
object XxxModule {
    @IntoSet
    @Provides
    fun provideFeature(navigator: DevPortalNavigator): DevPortalFeature =
        singleScreenFeature("Name shown in the menu", navigator) { back -> XxxScreen(back = back) }
}
```

The helper lives in `core/devportal/src/debug/java/com/mokelab/playground/core/devportal/SingleScreenFeature.kt` — `src/debug`, not `src/main`, because `:core:devportal` is always included in the build and its release variant must not reference DevPortal. It builds the navigation3 entry from a `FeatureKey(id)` **instance** key (`entry(key)`, not the reified `entry<T>`), so every feature can share one key type without declaring its own `object Root`. The `id` defaults to the menu name; **two enabled features with the same name crash at startup** with `An 'entry' with the key 'key' has already been added` — pass a distinct `id` to work around it. Never register `entry<FeatureKey>` (the class-key overload) anywhere: `EntryProviderScope.build()` resolves class keys first and would shadow every instance key.

Features that need **more than one screen** skip the helper and implement `DevPortalFeature` directly with their own key type and `entry<T>`; both styles coexist.

Screen composables are declared as `internal fun XxxScreen(back: () -> Unit)` and wrap their content in a `Scaffold` with a `TopAppBar`. Feature modules depend on `projects.core.devportal`, which pulls `libs.devportal.api` in through `debugApi` — so DevPortal reaches the debug compile classpath and never the release one (`app` uses `debugImplementation(libs.devportal)` plus the logcat modules). Note that because `Module.kt` sits in `src/main`, a feature module only compiles in the debug variant; that is why every feature stays commented out in committed state.

## Enabling / disabling a feature

In `settings.gradle.kts` the `include(":feature:...")` lines and in `app/build.gradle.kts` the `implementation(projects.feature....)` lines are **all commented out in committed state** (check HEAD). Uncomment both lines for the feature you want to try, build, then comment them back out before committing.

```kotlin
// settings.gradle.kts
include(":feature:compose:foundation:style")
// app/build.gradle.kts   — TYPESAFE_PROJECT_ACCESSORS is enabled
implementation(projects.feature.compose.foundation.style)
```

## Adding a new feature

1. Create the module under `feature/<area>/<name>/` (e.g. `feature/compose/foundation/flexbox`, `feature/m3/calendar`).
2. Copy `build.gradle.kts` from an existing feature (`feature/m3/calendar` is the reference). It applies `libs.plugins.mokelab.android.library.compose` + `libs.plugins.mokelab.hilt`, and the only thing to change in `android {}` is `namespace = "com.mokelab.playground.feature.<area>.<name>"` — SDK levels, Java 17 and Compose come from the convention plugin. Keep `implementation(projects.core.devportal)` — that is what brings in `DevPortalFeature` and `singleScreenFeature`.
3. Add `XxxScreen.kt` and `Module.kt` following the pattern above.
4. Register the module in `settings.gradle.kts` and `app/build.gradle.kts`, verify it runs, then comment both lines back out. `include(":core:devportal")` stays uncommented.

Work happens on `feature/<name>` branches and lands on `main` through pull requests (per the git history).
