package com.niksatyr.media2.local.di

import android.app.Service
import android.content.Context
import androidx.media2.common.SessionPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.media2.SessionPlayerConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import com.niksatyr.media2.local.DefaultMediaSessionCallback
import com.niksatyr.media2.local.LocalPlayerService
import com.niksatyr.media2.local.notification.ForegroundAwareNotificationListener
import com.niksatyr.media2.radio.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import java.lang.ref.WeakReference

@Module
@InstallIn(ServiceComponent::class)
internal object LocalSessionModule {

    @Provides
    @ServiceScoped
    fun provideSessionPlayer(player: Player): SessionPlayer {
        return SessionPlayerConnector(player)
    }

    @Provides
    @ServiceScoped
    fun provideMediaSessionCallback(): DefaultMediaSessionCallback {
        return DefaultMediaSessionCallback()
    }

    @Provides
    @ServiceScoped
    fun providePlayerNotificationManager(
        @ApplicationContext context: Context,
        notificationListener: PlayerNotificationManager.NotificationListener,
        mediaDescriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter,
        player: Player
    ): PlayerNotificationManager {
        NotificationUtil.createNotificationChannel(
            context,
            "player",
            R.string.app_name,
            R.string.app_name,
            NotificationUtil.IMPORTANCE_DEFAULT
        )

        val playerNotificationManager = PlayerNotificationManager.Builder(
            context,
            12,
            "player"
        )
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .build()

        playerNotificationManager.apply {
            setPlayer(player)

            setUseFastForwardAction(false)
            setUseFastForwardActionInCompactView(false)
            setUseRewindAction(false)
            setUseRewindActionInCompactView(false)

            setUseNextActionInCompactView(true)
            setUsePreviousActionInCompactView(true)
        }

        return playerNotificationManager
    }

    @Provides
    @ServiceScoped
    fun provideNotificationListener(service: Service): PlayerNotificationManager.NotificationListener {
        check(service is LocalPlayerService)
        return ForegroundAwareNotificationListener(WeakReference(service))
    }

    @Provides
    @ServiceScoped
    fun provideMediaDescriptionAdapter(@ApplicationContext context: Context): PlayerNotificationManager.MediaDescriptionAdapter {
        return DefaultMediaDescriptionAdapter(context)
    }

}