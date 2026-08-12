package com.mokelab.playground

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

/**
 * 全 Android モジュール共通のビルド設定。
 * ここを変えれば app / core / feature すべてに効く。
 * SDK / Java のレベルそのものは [AndroidConfig.kt] を参照。
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk {
            version = release(COMPILE_SDK) {
                minorApiLevel = COMPILE_SDK_MINOR
            }
        }

        defaultConfig.minSdk = MIN_SDK
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions.sourceCompatibility = JAVA_VERSION
        compileOptions.targetCompatibility = JAVA_VERSION
    }
}
