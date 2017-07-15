package ru.SnowVolf.convertx.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log

import ru.SnowVolf.convertx.settings.DefStrings

/**
 * Created by Snow Volf on 31.03.2017, 4:08
 */

object RuntimePermission {

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /**
     * Checks if the app has permission to write to device storage
     *
     *
     * If the app does not has permission then the user will be prompted to grant permissions

     * @param activity
     * *               context
     */
    fun verifyStoragePermissions(activity: Activity) {
        Log.i(DefStrings.GIRL, "RuntimePermission")
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        Log.i(DefStrings.GIRL, "Check : RuntimePermission")
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            Log.i(DefStrings.GIRL, "RequestPermissions : RuntimePermission")
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}
