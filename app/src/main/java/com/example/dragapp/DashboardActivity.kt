package com.example.dragapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dragapp.models.AppUsageModel
import com.example.dragapp.models.CheckAppsVisible
import com.example.dragapp.services.Notification
import com.example.dragapp.services.UserLocation
import com.example.dragapp.utils.Constants
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.activity_bnv)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)

        AppUsageModel.setContext(this)
        // CheckAppsVisible.setContext(this)
        //TODO:does not work requestUserLocation()
        askAppUsagePermission()
        createNotificationChannel()
        setAlarm()
    }

    fun askAppUsagePermission(){
        val appUsageModel = AppUsageModel()
        if (!appUsageModel.checkUsageStatePermission()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private fun requestUserLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (UserLocation().checkLocationPermission(this.applicationContext)) {
            if (UserLocation().isLocationEnabled(this.applicationContext)) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                        buildLocationCallback()
                        fusedLocationProviderClient =
                            LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.myLooper()
                        )
                    } else {
                        currentLatitude = location.latitude
                        currentLongitude = location.longitude
                    }
                }
            } else {
                Toast.makeText(
                    this.applicationContext,
                    "Please turn on location",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                this.startActivity(intent)
            }
        } else {
            UserLocation().requestPermissions(this)
        }
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                currentLatitude = locationResult!!.lastLocation.latitude
                currentLongitude = locationResult!!.lastLocation.longitude
            }
        }
    }

    private fun requestNewLocationData() {
        locationRequest = LocationRequest()
        UserLocation().requestNewLocationData(locationRequest)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestUserLocation()
    }

    fun setAlarm() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, Notification::class.java)
        var pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, setAlarmTime(8, 0).timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    fun setAlarmTime(hour: Int, minute: Int) : Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val cur: Calendar = Calendar.getInstance()
        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1)
        }
        // calendar.set(Calendar.MINUTE, Calendar.MINUTE+1)
        return calendar
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.CHANNEL_ID
            val descriptionText = "Used for Event Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(channel)
        }
    }
}