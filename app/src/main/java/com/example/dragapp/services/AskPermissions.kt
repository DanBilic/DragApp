package com.example.dragapp.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.dragapp.models.AppUsageModel
import com.example.dragapp.utils.Constants
import android.provider.Settings

class AskPermissions(private val context: Context, private val activity:Activity) {

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