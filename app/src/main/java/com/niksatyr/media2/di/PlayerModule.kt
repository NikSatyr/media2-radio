package com.niksatyr.media2.di

import android.content.Context
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsCollector
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.util.Clock
import com.niksatyr.media2.RadioMetadataUpdater
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
internal object PlayerModule {

    @Provides
    @ServiceScoped
    fun providePlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
        analyticsCollector: AnalyticsCollector
    ): Player {
        return ExoPlayer.Builder(context, DefaultRenderersFactory(context), DefaultMediaSourceFactory(context))
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(audioAttributes, true)
            .setWakeMode(C.WAKE_MODE_NETWORK)
            .setAnalyticsCollector(analyticsCollector)
            .build()
    }

    @Provides
    @ServiceScoped
    fun provideAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setAllowedCapturePolicy(C.ALLOW_CAPTURE_BY_NONE)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    @Provides
    @ServiceScoped
    fun provideAnalyticsCollector(radioMetadataUpdater: RadioMetadataUpdater): AnalyticsCollector {
        return AnalyticsCollector(Clock.DEFAULT).also {
            it.addListener(radioMetadataUpdater)
        }
    }

    @Provides
    @ServiceScoped
    fun provideRadioMetadataUpdater(): RadioMetadataUpdater {
        return RadioMetadataUpdater()
    }

}