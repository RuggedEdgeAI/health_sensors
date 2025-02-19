package com.isafemobile.test.sensor.heartrate.main

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeartRateViewModel(application: Application) : AndroidViewModel(application) {

    private val _heartRate: MutableStateFlow<Int> = MutableStateFlow(0)
    val heartRate = _heartRate.asStateFlow()

    private val handler = MyHandler()
    private val databaseManager by lazy { DatabaseManager.instance }

    fun getInitial() {
        viewModelScope.launch {
            databaseManager.setContext(getApplication())
            val hr = databaseManager.loadHR()
            _heartRate.emit(hr)
        }
    }

    fun startMonitoring() {
        databaseManager.registerHRObserver(handler)
        cancelDetection()
        startDetection()
    }

    private fun startDetection() {
        val iService = Intent()
        iService.setAction(ACTION_LOCAL_SOCKET)
        iService.putExtra("type", 0x0032.toShort())
        iService.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        val componentName = ComponentName(
            "com.sikey.commonservice",
            "com.sikey.commonservice.service.receiver.LocalSocketReceiver"
        )
        iService.setComponent(componentName)
        iService.putExtra("SensorType", 8.toByte())
        iService.putExtra("on", true)
        getApplication<Application>().sendBroadcast(iService, PERMISSION_LOCAL_SOCKET)
    }

    private fun cancelDetection() {
        val iService = Intent()
        iService.setAction(ACTION_LOCAL_SOCKET)
        iService.putExtra("type", 0x0032.toShort())
        iService.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        val componentName = ComponentName(
            "com.sikey.commonservice",
            "com.sikey.commonservice.service.receiver.LocalSocketReceiver"
        )
        iService.setComponent(componentName)
        iService.putExtra("SensorType", 8.toByte())
        iService.putExtra("on", false)
        getApplication<Application>().sendBroadcast(iService, PERMISSION_LOCAL_SOCKET)
    }

    private fun stopMonitoring() {
        cancelDetection()
        databaseManager.unregisterHRObserver()
    }

    override fun onCleared() {
        super.onCleared()
        stopMonitoring()
    }

    private inner class MyHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            viewModelScope.launch {
                val hr = databaseManager.loadHR()
                _heartRate.emit(hr)
            }
        }
    }

    private companion object {
        private const val ACTION_LOCAL_SOCKET = "com.sikey.localsocket"
        private const val PERMISSION_LOCAL_SOCKET = "com.sikey.permission.localsocket"
    }
}

