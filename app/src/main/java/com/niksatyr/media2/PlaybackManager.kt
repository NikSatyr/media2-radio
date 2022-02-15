package com.niksatyr.media2

import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.niksatyr.media2.local.LocalPlayerClient

internal class PlaybackManager(private val localPlayerClient: LocalPlayerClient) {

    private val player: Player? get() = localPlayerClient.player

    fun playDemo() {
        val metadata = MediaMetadata.Builder()
            .setTitle("Sample Track")
            .setArtist("Sample Artist")
            .setMediaUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3".toUri())
            .build()

        val mediaItem = MediaItem.Builder()
            .setMediaId("1")
            .setUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
            .setMediaMetadata(metadata)
            .build()

        player?.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

}