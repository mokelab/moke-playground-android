package com.mokelab.playground.feature.compose.webview.form

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import com.mokelab.android.devportal.api.DevPortalFeature
import com.mokelab.android.devportal.api.DevPortalNavigator
import com.mokelab.playground.feature.compose.foundation.style.StyleScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object Root

@Module
@InstallIn(ActivityRetainedComponent::class)
object ComposeStyleModule {
    @OptIn(ExperimentalMaterial3Api::class)
    @IntoSet
    @Provides
    fun provideComposeStyleFeature(
        navigator: DevPortalNavigator,
    ): DevPortalFeature {
        return object : DevPortalFeature {
            override val name: String = "Style API"
            override val installer: EntryProviderScope<Any>.() -> Unit = {
                entry<Root> {
                    StyleScreen(
                        back = { navigator.goBack() },
                    )
                }
            }
            override val root: Any get() = Root
        }
    }
}