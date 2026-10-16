package com.mokelab.playground

import org.gradle.api.JavaVersion

/**
 * 全モジュール共通の SDK / Java レベル。ここだけを書き換えれば全体に効く。
 */
internal const val COMPILE_SDK = 37
internal const val COMPILE_SDK_MINOR = 0
internal const val MIN_SDK = 24
internal const val TARGET_SDK = 37
internal val JAVA_VERSION = JavaVersion.VERSION_17
