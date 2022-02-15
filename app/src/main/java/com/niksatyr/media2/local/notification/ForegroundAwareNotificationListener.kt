package com.niksatyr.media2.local.notification

import android.app.Notification
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.niksatyr.media2.local.LocalPlayerService
import java.lang.ref.WeakReference

internal class ForegroundAwareNotificationListener(
    private val weakLocalPlayerService: WeakReference<LocalPlayerService>
) : PlayerNotificationManager.NotificationListener {

    private val localPlayerService: LocalPlayerService? get() = weakLocalPlayerService.get()

    private var isInForeground = false

    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        super.onNotificationPosted(notificationId, notification, ongoing)

        when (ongoing) {
            true -> startForegroundIfNeeded(notificationId, notification)
            false -> stopForegroundIfNeeded()
        }
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        super.onNotificationCancelled(notificationId, dismissedByUser)

        localPlayerService?.stopSelf()
    }

    private fun startForegroundIfNeeded(notificationId: Int, notification: Notification) {
        if (isInForeground) {
            return
        }

        localPlayerService?.startForeground(notificationId, notification)
        isInForeground = true
    }

    private fun stopForegroundIfNeeded() {
        if (!isInForeground) {
            return
        }

        localPlayerService?.stopForeground(false)
        isInForeground = false
    }

}