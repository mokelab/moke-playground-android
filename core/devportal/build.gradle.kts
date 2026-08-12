plugins {
    alias(libs.plugins.mokelab.android.library.compose)
}

android {
    namespace = "com.mokelab.playground.core.devportal"
}

dependencies {
    // @Composable が公開シグネチャに出るので api
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)

    // DevPortalFeature / DevPortalNavigator も公開シグネチャに出るので api。
    // ただし DevPortal は debug 限定なので debugApi にして release には伝播させない。
    // ヘルパー本体も src/debug に置いてあるので release バリアントは空になる。
    debugApi(libs.devportal.api)
}
