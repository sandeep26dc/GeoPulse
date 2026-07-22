package com.geopulse.app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geopulse.app.data.LocationState
import com.geopulse.app.data.NativeLocationManager

class MainActivity : ComponentActivity() {
    private var locationState = mutableStateOf(LocationState())

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            startTracking()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            GeoPulseDashboard(locationState.value)
        }
    }

    private fun startTracking() {
        val manager = NativeLocationManager(this)
        manager.startLocationUpdates { state ->
            locationState.value = state
        }
    }
}

val CyberBackground = Color(0xFF0B0F17)
val CyberCardBg = Color(0xFF141C2B)
val NeonCyan = Color(0xFF00F0FF)
val NeonPurple = Color(0xFF8A2BE2)
val NeonGreen = Color(0xFF00FF66)

@Composable
fun GeoPulseDashboard(state: LocationState) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "GEOPULSE",
                    color = NeonCyan,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "GPS TELEMETRY SUITE",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (state.isGpsActive) NeonGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (state.isGpsActive) "● GPS LIVE" else "○ SEARCHING",
                    color = if (state.isGpsActive) NeonGreen else Color.Red,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Center Speedometer Gauge
        Box(
            modifier = Modifier.size(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2 - 12.dp.toPx()

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonCyan.copy(alpha = 0.2f), Color.Transparent)
                    ),
                    radius = radius + 10.dp.toPx()
                )

                drawArc(
                    color = Color(0xFF1E293B),
                    startAngle = 135f,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = 14f)
                )

                val sweepAngle = ((state.speedKmh / 160f) * 270f).coerceAtMost(270f)
                drawArc(
                    brush = Brush.horizontalGradient(listOf(NeonCyan, NeonPurple)),
                    startAngle = 135f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 14f)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%.1f".format(state.speedKmh),
                    color = Color.White,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "KM / H",
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Telemetry Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TelemetryCard(title = "LATITUDE", value = "%.5f".format(state.latitude), unit = "DEG", modifier = Modifier.weight(1f))
            TelemetryCard(title = "LONGITUDE", value = "%.5f".format(state.longitude), unit = "DEG", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TelemetryCard(title = "ALTITUDE", value = "%.1f".format(state.altitude), unit = "METERS", modifier = Modifier.weight(1f))
            TelemetryCard(title = "ACCURACY", value = "±%.1f".format(state.accuracy), unit = "METERS", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Launch Maps Action
        Button(
            onClick = {
                val gmmIntentUri = Uri.parse("geo:${state.latitude},${state.longitude}?q=${state.latitude},${state.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                context.startActivity(mapIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "OPEN LIVE PIN IN MAPS",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun TelemetryCard(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CyberCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(text = unit, color = NeonCyan.copy(alpha = 0.7f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
