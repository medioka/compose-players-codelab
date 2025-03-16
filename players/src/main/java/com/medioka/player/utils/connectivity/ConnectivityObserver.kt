package com.medioka.player.utils.connectivity

import kotlinx.coroutines.flow.StateFlow

interface ConnectivityObserver {
    val isNetworkConnectedFlow: StateFlow<Boolean>
    val isNetworkConnected: Boolean
    fun register()
    fun unregister()
}
