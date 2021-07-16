package com.example.dragapp.fragments.appUsage

data class AppUsageData(
    val packageName: String,
    val appName: String,
    val picture: Int,
    val countUsage: Int,
    val lastTimeUsed: String,
    val totalTimeVisible: String
)