package com.niksatyr.media2.local

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.Player
import timber.log.Timber
import java.lang.ref.WeakReference

internal class LocalPlayerClient(
    private val weakActivity: WeakReference<ComponentActivity>
) : LifecycleObserver {

    var isPlayerReady = false
        get() = player != null
        private set

    var player: Player? = null
        private set

    private val activity: ComponentActivity? get() = weakActivity.get()

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            player = (service as? LocalPlayerService.PlayerBinder)?.player
            Timber.i("Player client connected, player is ready: $isPlayerReady")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            player = null
            Timber.i("Player client disconnected")
        }
    }

    init {
        activity?.lifecycle?.addObserver(this)

        Timber.i("Binding service on init")

        val playerServiceIntent = Intent(activity, LocalPlayerService::class.java).also {
            it.action = LocalPlayerService.ACTION_BIND_PLAYER
        }

        activity?.bindService(playerServiceIntent, serviceConnection, Service.BIND_AUTO_CREATE)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Timber.i("Unbinding service since activity is being destroyed")
        unbind()
        activity?.lifecycle?.removeObserver(this)
    }

    private fun unbind() {
        activity?.unbindService(serviceConnection)
        player = null
    }

}