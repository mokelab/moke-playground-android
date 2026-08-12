plugins {
    alias(libs.plugins.mokelab.android.application.compose)
    alias(libs.plugins.mokelab.hilt)
}

android {
    namespace = "com.mokelab.playground.android"

    defaultConfig {
        applicationId = "com.mokelab.playground.android"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // implementation(projects.feature.compose.webview.form)
    // implementation(projects.feature.m3.calendar)
    // implementation(projects.feature.compose.foundation.flexbox)
    // implementation(projects.feature.compose.foundation.style)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    debugImplementation(libs.devportal)
    debugImplementation(libs.logcat.core)
    debugImplementation(libs.logcat.basic)


    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
