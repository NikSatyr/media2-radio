package com.niksatyr.media2.local.di

import com.niksatyr.media2.PlaybackManager
import com.niksatyr.media2.local.LocalPlayerClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
internal object PlaybackModule {

    @Provides
    @ActivityScoped
    fun providePlaybackManager(localPlayerClient: LocalPlayerClient): PlaybackManager {
        return PlaybackManager(localPlayerClient)
    }

}