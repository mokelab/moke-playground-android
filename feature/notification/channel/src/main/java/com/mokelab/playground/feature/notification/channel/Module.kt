package com.mokelab.playground.feature.notification.channel

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
object NotificationChannelModule {
    @IntoSet
    @Provides
    fun provideFeature(navigator: DevPortalNavigator): DevPortalFeature =
        singleScreenFeature("Notification Channel", navigator) { back ->
            NotificationChannelScreen(back = back)
        }
}
