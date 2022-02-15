package com.niksatyr.media2.local

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.media2.common.SessionPlayer
import androidx.media2.session.MediaLibraryService
import androidx.media2.session.MediaSession
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
internal class LocalPlayerService : MediaLibraryService() {

    private val binder = PlayerBinder()

    @Inject
    lateinit var player: Player

    @Inject
    lateinit var sessionPlayer: SessionPlayer

    @Inject
    lateinit var sessionCallback: DefaultMediaSessionCallback

    @Inject
    lateinit var playerNotificationManager: PlayerNotificationManager

    private lateinit var session: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()
        Timber.i("LocalPlayerService created")

        session = MediaLibrarySession.Builder(
            this,
            sessionPlayer,
            Executors.newSingleThreadExecutor(),
            sessionCallback
        )
            .build()

        playerNotificationManager.setMediaSessionToken(session.sessionCompatToken)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("LocalPlayerService destroyed")

        playerNotificationManager.setPlayer(null)
        session.close()
        sessionPlayer.close()
        player.release()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session
    }

    override fun onBind(intent: Intent): IBinder? = when (intent.action) {
        ACTION_BIND_PLAYER -> binder
        else -> super.onBind(intent)
    }

    inner class PlayerBinder : Binder() {
        val player: Player get() = this@LocalPlayerService.player
    }

    companion object {
        const val ACTION_BIND_PLAYER = "MEDIA.ACTION_BIND_PLAYER"
    }

}