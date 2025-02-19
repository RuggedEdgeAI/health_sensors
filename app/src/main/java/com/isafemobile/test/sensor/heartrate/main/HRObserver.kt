package com.isafemobile.test.sensor.heartrate.main

import android.database.ContentObserver
import android.hardware.Sensor
import android.net.Uri
import android.os.Handler

class HRObserver(handler: Handler?) : ContentObserver(handler) {
    init {
        mHandler = handler
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        mHandler!!.postDelayed({ mHandler!!.sendEmptyMessage(Sensor.TYPE_HEART_RATE) }, 1000)
    }

    companion object {
        private var mHandler: Handler? = null
    }
}
