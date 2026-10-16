plugins {
    alias(libs.plugins.mokelab.android.library.compose)
}

android {
    namespace = "com.mokelab.playground.core.ui"
}

dependencies {
    // @Composable / Modifier が公開シグネチャに出るので api
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
}
