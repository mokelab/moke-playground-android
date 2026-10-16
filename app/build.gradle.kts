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
    // Enabled via `features=` in local.properties (see settings.gradle.kts)
    @Suppress("UNCHECKED_CAST")
    (gradle.extra["playground.features"] as List<String>).forEach { name ->
        implementation(project(":feature:$name"))
    }

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
