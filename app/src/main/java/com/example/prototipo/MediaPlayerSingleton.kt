package com.example.projeto.ui

import android.content.Context
import android.media.MediaPlayer

object MediaPlayerSingleton {
    private var mediaPlayer: MediaPlayer? = null

    fun start(context: Context, resId: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId)
        }
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
