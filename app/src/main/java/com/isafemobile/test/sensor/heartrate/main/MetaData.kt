package com.isafemobile.test.sensor.heartrate.main

import android.net.Uri

object MetaData {
    private const val AUTHORITIES: String = "com.sikey.WatchCommonProvider"
    private const val CONTENT_URI: String = "content://$AUTHORITIES/"
    private const val HR_TABLE_NAME = "hr"
    val HR_TABLE_URI: Uri = Uri.parse(CONTENT_URI + HR_TABLE_NAME)

    object HR {
        const val CURRENT: String = "current"
        const val UPDATE_TIME: String = "updatetime"
    }
}
