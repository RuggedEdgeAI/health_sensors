package com.isafemobile.test.sensor.heartrate.main

import android.net.Uri
import androidx.core.net.toUri

object MetaData {
    private const val AUTHORITIES: String = "com.sikey.WatchCommonProvider"
    private const val CONTENT_URI: String = "content://$AUTHORITIES/"
    private const val HR_TABLE_NAME = "hr"
    private const val SPO_TABLE_NAME = "spo"
    
    val HR_TABLE_URI: Uri = (CONTENT_URI + HR_TABLE_NAME).toUri()
    val SPO_TABLE_URI: Uri = (CONTENT_URI + SPO_TABLE_NAME).toUri()

    object HR {
        const val CURRENT: String = "current"
        const val UPDATE_TIME: String = "updatetime"
    }

    object SpO2 {
        const val CURRENT: String = "current"
        const val UPDATE_TIME: String = "updatetime"
    }
}
