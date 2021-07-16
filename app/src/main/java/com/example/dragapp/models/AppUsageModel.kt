package com.example.dragapp.models

import android.Manifest
import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process.myUid
import androidx.annotation.RequiresApi
import androidx.core.app.AppOpsManagerCompat.MODE_ALLOWED
import com.example.dragapp.R
import com.example.dragapp.utils.AppUsageData
import com.example.dragapp.utils.SocialApps
import java.text.SimpleDateFormat
import java.util.*


class AppUsageModel {

    companion object {
        private lateinit var context: Context
        val appUsageList: MutableList<AppUsageData> = mutableListOf()
        val appUsageSelected: MutableList<AppUsageData> = mutableListOf()

        fun setContext(con: Context) {
            context = con
        }
    }

    fun checkUsageStatePermission(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), context.getPackageName())
        } else {
            TODO("VERSION.SDK_INT < Q")
        }
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == MODE_ALLOWED
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getUsageStatsSocialAppsDay() {
        var usageStatsManager: UsageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        var queryUsageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            cal.timeInMillis,
            System.currentTimeMillis()
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (i in queryUsageStats.indices) {
                if (queryUsageStats[i].lastTimeUsed != 0L && currentTime(queryUsageStats[i].lastTimeUsed)) {
                    for (apps in SocialApps.socialAppsList) {
                        if (queryUsageStats[i].packageName.contains(apps)) {
                            var newAppName = setAppName(queryUsageStats[i].packageName, apps)
                            var appUsageD = AppUsageData(
                                queryUsageStats[i].packageName,
                                newAppName,
                                getPictureApp(newAppName),
                                0,
                                convertDateTime(queryUsageStats[i].lastTimeUsed),
                                convertTime(queryUsageStats[i].totalTimeVisible)
                            )
                            appUsageList.add(appUsageD)
                        }
                    }
                }
            }

        }
    }

    fun groupList(){
        var sortedAppUsageList = appUsageList.groupBy { it.appName }
        val containApps: MutableList<String> = mutableListOf()
        for (sortedApp in sortedAppUsageList.values) {
            if(appUsageSelected.size>0) {
                for (nameAvailable in containApps) {
                    if (nameAvailable != sortedApp[sortedApp.lastIndex].appName) {
                        appUsageSelected.add(
                            AppUsageData(
                                sortedApp[sortedApp.lastIndex].packageName,
                                sortedApp[sortedApp.lastIndex].appName,
                                sortedApp[sortedApp.lastIndex].picture,
                                sortedApp.size,
                                sortedApp[sortedApp.lastIndex].lastTimeUsed,
                                sortedApp[sortedApp.lastIndex].totalTimeVisible
                            )
                        )
                    }
                }
            } else {
                containApps.add(sortedApp[sortedApp.lastIndex].appName)
                appUsageSelected.add(
                    AppUsageData(
                        sortedApp[sortedApp.lastIndex].packageName,
                        sortedApp[sortedApp.lastIndex].appName,
                        sortedApp[sortedApp.lastIndex].picture,
                        sortedApp.size,
                        sortedApp[sortedApp.lastIndex].lastTimeUsed,
                        sortedApp[sortedApp.lastIndex].totalTimeVisible
                    )
                )
            }


        }

        /*for (element in appUsageSelected) {
            println("----------------------------------------------------------")
            println("package Name : " + element.packageName)
            println("app Name : " + element.appName)
            println("last time used : " + element.lastTimeUsed)
            println("total time visible : " + element.totalTimeVisible)
        }*/
    }

    private fun setAppName(packageName: String, appName: String): String {
        var appN = ""
        if (packageName.equals("com.facebook.orca")) {
            appN = "facebook messenger"
        } else if (packageName.equals("com.facebook.katana")) {
            appN = "facebook"
        } else {
            appN = appName
        }
        return appN
    }

    private fun getPictureApp(appName: String): Int {
        var picture = 0
        if (appName.equals("youtube")) {
            picture = R.drawable.youtube_icon
        } else if (appName.equals("whatsapp")) {
            picture = R.drawable.whatsapp_icon
        } else if (appName.equals("snapchat")) {
            picture = R.drawable.snapchat_icon
        } else if (appName.equals("tikTok")) {
            picture = R.drawable.tiktok_icon
        } else if (appName.equals("instagram")) {
            picture = R.drawable.instagram_icon
        } else if (appName.equals("facebook")) {
            picture = R.drawable.facebook_icon
        } else if (appName.equals("telegram")) {
            picture = R.drawable.telegram_icon
        } else if (appName.equals("twitter")) {
            picture = R.drawable.twitter_icon
        } else if (appName.contains("messenger")) {
            picture = R.drawable.messenger_icon
        }
        return picture

    }

    private fun convertDateTime(lastTimeUsed: Long): String {
        var date = Date(lastTimeUsed)
        var format = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        return format.format(date)
    }

    private fun convertTime(lastTimeUsed: Long): String {
        var date = Date(lastTimeUsed)
        var format = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        return format.format(date)
    }

    private fun currentTime(lastTimeUsed: Long): Boolean {
        var bool = false
        var firstDay: Calendar = GregorianCalendar(2021, 1, 1)
        var lastTimeUsedDate = Date(lastTimeUsed)
        var last: Calendar = Calendar.getInstance()
        last.set(1900 + lastTimeUsedDate.year, lastTimeUsedDate.month, lastTimeUsedDate.date)
        if (last.after(firstDay)) {
            bool = true
        }
        return bool
    }
}

