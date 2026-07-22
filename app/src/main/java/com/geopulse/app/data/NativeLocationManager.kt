package com.geopulse.app.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*

data class LocationState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val speedKmh: Float = 0f,
    val accuracy: Float = 0f,
    val bearing: Float = 0f,
    val isGpsActive: Boolean = false,
    val error: String? = null
)

class NativeLocationManager(context: Context) {
    private val client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 1000L
    ).apply {
        setMinUpdateIntervalMillis(500L)
    }.build()

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(onUpdate: (LocationState) -> Unit) {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    onUpdate(
                        LocationState(
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                            altitude = loc.altitude,
                            speedKmh = (loc.speed * 3.6f),
                            accuracy = loc.accuracy,
                            bearing = loc.bearing,
                            isGpsActive = true,
                            error = null
                        )
                    )
                }
            }
        }

        try {
            client.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
        } catch (e: Exception) {
            onUpdate(LocationState(error = e.localizedMessage ?: "GPS Initialization Failed"))
        }
    }
}
