package com.niksatyr.media2

import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.niksatyr.media2.local.LocalPlayerClient

internal class PlaybackManager(private val localPlayerClient: LocalPlayerClient) {

    private val player: Player? get() = localPlayerClient.player

    fun playRadio() {
        val radioItem = MediaItem.fromUri("http://a.files.bbci.co.uk/media/live/manifesto/audio/simulcast/hls/nonuk/sbr_low/ak/bbc_radio_one.m3u8")

        player?.apply {
            setMediaItem(radioItem)
            prepare()
            play()
        }
    }

}