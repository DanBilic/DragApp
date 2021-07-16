package com.example.dragapp.models

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE

class CheckAppsVisible {
    companion object {

        private lateinit var context: Context

        fun setContext(con: Context) {
            context = con
        }
    }

    fun startCheck(){
       var activityManager: ActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        var appProcess: List <RunningAppProcessInfo> = activityManager.runningAppProcesses

        for (element in appProcess) {
            println("apps : " + element.processName)
        }

    }
}