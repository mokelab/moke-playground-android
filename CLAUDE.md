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

## Architecture

**There is no MainActivity.** The launcher activity is `com.mokelab.android.devportal.DevPortalActivity`, provided by the DevPortal library and declared in `app/src/main/AndroidManifest.xml`. The app module only owns `PlaygroundApplication` (`@HiltAndroidApp`); every screen lives in a feature module.

Each feature module contributes a `DevPortalFeature` from its `Module.kt` via Hilt's `@IntoSet`. DevPortal collects that set to build its menu and renders each screen as a navigation3 entry. The established pattern (see `feature/compose/foundation/style/src/main/java/.../Module.kt` and `feature/m3/calendar/src/main/java/.../Module.kt`):

```kotlin
object Root

@Module
@InstallIn(ActivityRetainedComponent::class)
object XxxModule {
    @IntoSet
    @Provides
    fun provideXxxFeature(navigator: DevPortalNavigator): DevPortalFeature =
        object : DevPortalFeature {
            override val name: String = "Name shown in the menu"
            override val installer: EntryProviderScope<Any>.() -> Unit = {
                entry<Root> { XxxScreen(back = { navigator.goBack() }) }
            }
            override val root: Any get() = Root
        }
}
```

Screen composables are declared as `internal fun XxxScreen(back: () -> Unit)` and wrap their content in a `Scaffold` with a `TopAppBar`. DevPortal is a `debugImplementation` dependency only (`libs.devportal.api` in feature modules; `libs.devportal` plus the logcat modules in `app`), so it never ships in a release build.

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
2. Copy `build.gradle.kts` from an existing feature: `android-library` + `kotlin-compose` + `ksp` + `hilt`, namespace `com.mokelab.playground.feature.<area>.<name>`, minSdk 24 / compileSdk 36, Java 11 (only `app` uses Java 17).
3. Add `XxxScreen.kt` and `Module.kt` following the pattern above.
4. Register the module in `settings.gradle.kts` and `app/build.gradle.kts`, verify it runs, then comment both lines back out.

Work happens on `feature/<name>` branches and lands on `main` through pull requests (per the git history).
