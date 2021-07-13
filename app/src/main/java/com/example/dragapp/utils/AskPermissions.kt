package com.example.dragapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dragapp.services.UserLocation
import com.google.android.gms.location.*

class AskPermissions(private val context: Context, private val activity:Activity) {

    fun askAppUsagePermission(){
        /*val appUsageModel = AppUsageModel()
        if (!appUsageModel.checkUsageStatePermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }*/
    }

    fun setupGalleryPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            Constants.RECORD_REQUEST_CODE
        )
    }


}