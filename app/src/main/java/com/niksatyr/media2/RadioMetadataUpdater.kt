package com.niksatyr.media2

import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.analytics.AnalyticsListener
import kotlinx.coroutines.*

internal class RadioMetadataUpdater : AnalyticsListener {

    override fun onEvents(player: Player, events: AnalyticsListener.Events) {
        super.onEvents(player, events)

        if (!events.contains(AnalyticsListener.EVENT_IS_PLAYING_CHANGED)) {
            return
        }

        setupCustomMediaMetadata(player)
    }

    private fun setupCustomMediaMetadata(player: Player) {
        GlobalScope.launch(Dispatchers.IO) {
            val customMetadata = getCustomMetadata()

            withContext(Dispatchers.Main) {
                player.playlistMetadata = customMetadata
            }
        }
    }

    private suspend fun getCustomMetadata(): MediaMetadata {
        delay(1000) // Simulate network delay

        return MediaMetadata.Builder()
            .setTitle("Sample title")
            .setArtist("Sample artist")
            .setArtworkUri("https://www.akc.org/wp-content/uploads/2017/11/Pembroke-Welsh-Corgi-standing-outdoors-in-the-fall.jpg".toUri())
            .build()
    }

}