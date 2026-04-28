package com.mokelab.playground.feature.m3.calendar

import androidx.navigation3.runtime.EntryProviderScope
import com.mokelab.android.devportal.api.DevPortalFeature
import com.mokelab.android.devportal.api.DevPortalNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

object Root

@Module
@InstallIn(ActivityRetainedComponent::class)
object CalendarModule {
    @IntoSet
    @Provides
    fun provideM3CalendarFeature(
        navigator: DevPortalNavigator,
    ): DevPortalFeature {
        return object : DevPortalFeature {
            override val name: String = "M3 Calendar"
            override val installer: EntryProviderScope<Any>.() -> Unit = {
                entry<Root> {
                    CalendarScreen(
                        back = { navigator.goBack() },
                    )
                }
            }
            override val root: Any get() = Root
        }
    }
}