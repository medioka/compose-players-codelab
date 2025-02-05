package com.medioka.player.callback

interface PlaybackStateCallback {
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun getBufferPercentage(): Int
}