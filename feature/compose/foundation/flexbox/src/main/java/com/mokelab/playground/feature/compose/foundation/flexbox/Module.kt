package com.mokelab.playground.feature.compose.webview.form

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import com.mokelab.android.devportal.api.DevPortalFeature
import com.mokelab.android.devportal.api.DevPortalNavigator
import com.mokelab.playground.feature.compose.foundation.flexbox.FlexBoxScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object Root

@Module
@InstallIn(ActivityRetainedComponent::class)
object FlexboxModule {
    @OptIn(ExperimentalMaterial3Api::class)
    @IntoSet
    @Provides
    fun provideWebViewFormFeature(
        navigator: DevPortalNavigator,
    ): DevPortalFeature {
        return object : DevPortalFeature {
            override val name: String = "Flexbox"
            override val installer: EntryProviderScope<Any>.() -> Unit = {
                entry<Root> {
                    FlexBoxScreen(
                        back = { navigator.goBack() },
                    )
                }
            }
            override val root: Any get() = Root
        }
    }
}