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

class HealthViewModel(application: Application) : AndroidViewModel(application) {

    private val _heartRate: MutableStateFlow<Int> = MutableStateFlow(0)
    val heartRate = _heartRate.asStateFlow()

    private val _spO2: MutableStateFlow<Int> = MutableStateFlow(0)
    val spO2 = _spO2.asStateFlow()

    private val handler = MyHandler()
    private val databaseManager by lazy { DatabaseManager.instance }

    fun getInitial() {
        viewModelScope.launch {
            databaseManager.setContext(getApplication())
            val hr = databaseManager.loadHR()
            val spo = databaseManager.loadSpO2()
            _heartRate.emit(hr)
            _spO2.emit(spo)
        }
    }

    fun startMonitoring() {
        databaseManager.registerHRObserver(handler)
        databaseManager.registerSpO2Observer(handler)
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
        
        // Start HR monitoring
        iService.putExtra("SensorType", 8.toByte())
        iService.putExtra("on", true)
        getApplication<Application>().sendBroadcast(iService, PERMISSION_LOCAL_SOCKET)
        
        // Start SpO2 monitoring
        iService.putExtra("SensorType", 9.toByte())
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
        
        // Stop HR monitoring
        iService.putExtra("SensorType", 8.toByte())
        iService.putExtra("on", false)
        getApplication<Application>().sendBroadcast(iService, PERMISSION_LOCAL_SOCKET)
        
        // Stop SpO2 monitoring
        iService.putExtra("SensorType", 9.toByte())
        iService.putExtra("on", false)
        getApplication<Application>().sendBroadcast(iService, PERMISSION_LOCAL_SOCKET)
    }

    private fun stopMonitoring() {
        cancelDetection()
        databaseManager.unregisterHRObserver()
        databaseManager.unregisterSpO2Observer()
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
                val spo = databaseManager.loadSpO2()
                _heartRate.emit(hr)
                _spO2.emit(spo)
            }
        }
    }

    private companion object {
        private const val ACTION_LOCAL_SOCKET = "com.sikey.localsocket"
        private const val PERMISSION_LOCAL_SOCKET = "com.sikey.permission.localsocket"
    }
}

