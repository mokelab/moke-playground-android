package com.mokelab.playground

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

/**
 * 全 Android モジュール共通のビルド設定。
 * ここを変えれば app / core / feature すべてに効く。
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }

        defaultConfig.minSdk = 24
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
    }
}
