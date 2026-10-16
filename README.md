# MokePlayground

A playground for experimenting with Android APIs — Jetpack Compose, Material 3, and friends.

Each experiment lives in its own Android library module under `feature/` and shows up as a menu entry in [mokelab DevPortal](https://github.com/mokelab/android-devportal), which serves as the app's launcher screen.

## Trying a feature

Feature modules are not built by default. Pick the ones you want in `local.properties` (it is git-ignored, so this never gets committed):

```properties
features=compose:foundation:flexbox,compose:foundation:style
```

Each entry is the module's path under `feature/`, with `:` as the separator, and entries are comma-separated. After editing, sync Gradle (or just run a build) and install:

```bash
./gradlew installDebug
```

A name that doesn't match a module fails the build immediately with an error pointing at the missing directory.

> **Note:** feature modules compile only in the **debug** variant. Leave `features` empty (or remove it) for release builds.

## Available features

| `features` entry | Menu name |
|---|---|
| `compose:foundation:flexbox` | Flexbox |
| `compose:foundation:style` | Style API |
| `compose:webview:form` | WebView form(Compose) |
| `m3:calendar` | M3 Calendar |
| `m3:textfield` | M3 TextFieldState |
| `notification:permission` | Notification Permission |

## Adding a feature

1. Create a module under `feature/<area>/<name>/`, copying `build.gradle.kts` from `feature/m3/calendar` and changing only its `namespace`.
2. Add the screen composable and a Hilt `Module.kt` that contributes a `DevPortalFeature` (use `singleScreenFeature()` for one-screen features).
3. Add it to `features=` in `local.properties` and run it. No changes to `settings.gradle.kts` or `app/build.gradle.kts` are needed.
4. Add a row to the table above.

See [CLAUDE.md](CLAUDE.md) for details on the architecture and build logic.

## Commands

```bash
./gradlew assembleDebug                # build
./gradlew installDebug                 # install on a device
./gradlew test                         # unit tests
./gradlew connectedDebugAndroidTest    # instrumented tests (device/emulator required)
./gradlew lint                         # Android Lint
```
