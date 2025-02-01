package com.medioka.player.callback

interface ControlsCallback {
    fun play()
    fun pause()
    fun next()
    fun previous()
    fun seekForward()
    fun seekBackward()
}