package com.mokelab.playground.feature.m3.textfield

import com.mokelab.android.devportal.api.DevPortalFeature
import com.mokelab.android.devportal.api.DevPortalNavigator
import com.mokelab.playground.core.devportal.singleScreenFeature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object TextFieldModule {
    @IntoSet
    @Provides
    fun provideFeature(navigator: DevPortalNavigator): DevPortalFeature =
        singleScreenFeature("M3 TextFieldState", navigator) { back -> TextFieldScreen(back = back) }
}
