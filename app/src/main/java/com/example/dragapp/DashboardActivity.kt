package com.example.dragapp

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dragapp.services.UserLocation
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.activity_bnv)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)

        requestUserLocation()
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
}