package com.mokelab.playground

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

/**
 * convention plugin から `gradle/libs.versions.toml` を引くための拡張。
 * precompiled script plugin ではないので typesafe な `libs` アクセサは使えない。
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")
