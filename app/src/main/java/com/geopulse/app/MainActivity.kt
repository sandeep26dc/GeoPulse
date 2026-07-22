package com.geopulse.app

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
            GeoPulseLuxuryDashboard(locationState.value)
        }
    }

    private fun startTracking() {
        val manager = NativeLocationManager(this)
        manager.startLocationUpdates { state ->
            locationState.value = state
        }
    }
}

// 💎 Executive Obsidian & Titanium Palette
val ObsidianBg = Color(0xFF05070A)
val SlateCard = Color(0xFF0F141C)
val TitaniumBorder = Color(0xFF1E293B)
val TextPrimary = Color(0xFFF8FAFC)
val TextMuted = Color(0xFF64748B)
val PlatinumAccent = Color(0xFF38BDF8)
val EmeraldLive = Color(0xFF10B981)

@Composable
fun GeoPulseLuxuryDashboard(state: LocationState) {
    val context = LocalContext.current

    // Smooth gauge animation for luxury feel
    val animatedSpeed by animateFloatAsState(
        targetValue = state.speedKmh,
        animationSpec = tween(durationMillis = 600),
        label = "SpeedAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Executive Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "G E O P U L S E",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 4.sp
                )
                Text(
                    text = "PRECISION TELEMETRY",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (state.isGpsActive) EmeraldLive else Color.Red)
                )
                Text(
                    text = if (state.isGpsActive) "LIVE FIX" else "SEARCHING",
                    color = if (state.isGpsActive) TextPrimary else TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Minimalist Luxury Instrument Cluster (Speed & Heading)
        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2 - 16.dp.toPx()

                // Thin Titanium Outer Ring
                drawCircle(
                    color = TitaniumBorder,
                    radius = radius,
                    style = Stroke(width = 2f)
                )

                // Track Arc
                drawArc(
                    color = SlateCard,
                    startAngle = 140f,
                    sweepAngle = 260f,
                    useCenter = false,
                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                )

                // Fluid Precision Arc
                val sweepAngle = ((animatedSpeed / 180f) * 260f).coerceAtMost(260f)
                drawArc(
                    color = PlatinumAccent,
                    startAngle = 140f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "%.0f".format(animatedSpeed),
                    color = TextPrimary,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "KM / H",
                    color = PlatinumAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "BEARING  %.0f°".format(state.bearing),
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Precision Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ExecutiveMetricTile(
                label = "LATITUDE",
                value = "%.5f".format(state.latitude),
                unit = "N / S",
                modifier = Modifier.weight(1f)
            )
            ExecutiveMetricTile(
                label = "LONGITUDE",
                value = "%.5f".format(state.longitude),
                unit = "E / W",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ExecutiveMetricTile(
                label = "ALTITUDE",
                value = "%.1f".format(state.altitude),
                unit = "METERS",
                modifier = Modifier.weight(1f)
            )
            ExecutiveMetricTile(
                label = "SIGNAL MARGIN",
                value = "±%.1f".format(state.accuracy),
                unit = "METERS",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sleek Map Navigation Bar
        OutlinedButton(
            onClick = {
                val gmmIntentUri = Uri.parse("geo:${state.latitude},${state.longitude}?q=${state.latitude},${state.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                context.startActivity(mapIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, TitaniumBorder),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
            Text(
                text = "EXPORT COORDINATES TO MAPS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun ExecutiveMetricTile(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(SlateCard)
            .border(1.dp, TitaniumBorder, RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                color = TextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = unit,
                color = PlatinumAccent.copy(alpha = 0.8f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
