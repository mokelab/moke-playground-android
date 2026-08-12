package com.mokelab.playground

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Compose コンパイラプラグインの適用、buildFeatures.compose の有効化、
 * および Compose の基本ライブラリの依存追加。
 *
 * material-icons のような「基本」に含めないものは各モジュール側で宣言する。
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension,
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.buildFeatures.compose = true

    dependencies {
        val bom = platform(libs.findLibrary("androidx-compose-bom").get())
        add("implementation", bom)
        add("androidTestImplementation", bom)

        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())

        // @Preview のレンダリングと Compose テストに必要なもの
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
        add("androidTestImplementation", libs.findLibrary("androidx-compose-ui-test-junit4").get())
    }
}
