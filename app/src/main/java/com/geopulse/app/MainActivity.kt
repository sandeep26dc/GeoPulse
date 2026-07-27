package com.geopulse.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.geopulse.app.data.LocationState
import com.geopulse.app.data.NativeLocationManager

class MainActivity : ComponentActivity() {
    private var locationState = mutableStateOf(LocationState())
    private lateinit var locationManager: NativeLocationManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            startTracking()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = NativeLocationManager(this)

        setContent {
            GeoPulseAdvancedDashboard(locationState.value)
        }

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startTracking()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun startTracking() {
        locationManager.startLocationUpdates { state ->
            locationState.value = state
        }
    }
}

// 💎 Glassmorphic Luxury Design System Colors
val DeepObsidian = Color(0xFF030508)
val GlassSurface = Color(0xFF0C1017).copy(alpha = 0.65f)
val GlassBorder = Color(0xFF38BDF8).copy(alpha = 0.15f)
val GlassBorderActive = Color(0xFF38BDF8).copy(alpha = 0.4f)
val TextLight = Color(0xFFF1F5F9)
val TextDim = Color(0xFF64748B)
val NeonCyan = Color(0xFF38BDF8)
val NeonEmerald = Color(0xFF10B981)

@Composable
fun GeoPulseAdvancedDashboard(state: LocationState) {
    val context = LocalContext.current

    // Pulsing animation for the live tracking beacon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beaconPulse"
    )

    // High-precision spring animation for speedometer value changes
    val animatedSpeed by animateFloatAsState(
        targetValue = state.speedKmh,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "speedSpring"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepObsidian)
    ) {
        // Ambient background glowing gradient blobs for glass depth
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(NeonCyan.copy(alpha = 0.05f), CircleShape)
                .blur(80.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(350.dp)
                .offset(x = 50.dp, y = 50.dp)
                .background(Color(0xFF6366F1).copy(alpha = 0.04f), CircleShape)
                .blur(100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
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
                        color = TextLight,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 5.sp
                    )
                    Text(
                        text = "QUANTUM TELEMETRY ENGINE",
                        color = TextDim,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.5.sp
                    )
                }

                // Glass Status Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassSurface)
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                if (state.isGpsActive) NeonEmerald.copy(alpha = pulseAlpha) 
                                else Color.Red.copy(alpha = pulseAlpha)
                            )
                    )
                    Text(
                        text = if (state.isGpsActive) "SYNCED" else "SEARCHING",
                        color = if (state.isGpsActive) NeonEmerald else Color.Red,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Glassmorphic Speedometer Cluster with Rotating Rings
            Box(
                modifier = Modifier.size(250.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background decorative ambient ring
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width / 2 - 12.dp.toPx()

                    // Outer glass track
                    drawCircle(
                        color = Color.White.copy(alpha = 0.03f),
                        radius = radius,
                        style = Stroke(width = 12.dp.toPx())
                    )

                    // Track Arc Base
                    drawArc(
                        color = Color.White.copy(alpha = 0.05f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Dynamic Neon Progress Arc
                    val sweep = ((animatedSpeed / 160f).coerceIn(0f, 1f)) * 270f
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.3f), NeonCyan)
                        ),
                        startAngle = 135f,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                // Center Value Readout
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "%.0f".format(animatedSpeed),
                        color = TextLight,
                        fontSize = 62.sp,
                        fontWeight = FontWeight.ExtraLight,
                        fontFamily = FontFamily.SansSerif
                    )
                    Text(
                        text = "KM / H",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "HEADING  %.0f°".format(state.bearing),
                        color = TextDim,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Glass Metric Cards Grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassMetricCard(
                        label = "LATITUDE",
                        value = "%.5f".format(state.latitude),
                        unit = "N / S",
                        modifier = Modifier.weight(1f)
                    )
                    GlassMetricCard(
                        label = "LONGITUDE",
                        value = "%.5f".format(state.longitude),
                        unit = "E / W",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassMetricCard(
                        label = "ALTITUDE",
                        value = "%.1f".format(state.altitude),
                        unit = "METERS",
                        modifier = Modifier.weight(1f)
                    )
                    GlassMetricCard(
                        label = "SIGNAL PRECISION",
                        value = "±%.1f".format(state.accuracy),
                        unit = "METERS",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Luxury Frosted Export Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(16.dp, RoundedCornerShape(16.dp), ambientColor = NeonCyan.copy(alpha = 0.1f))
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassSurface)
                    .border(1.dp, GlassBorderActive, RoundedCornerShape(16.dp))
                    .clickable {
                        val uri = Uri.parse("geo:${state.latitude},${state.longitude}?q=${state.latitude},${state.longitude}")
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "EXPORT TELEMETRY TO MAPS",
                        color = TextLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GlassMetricCard(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(GlassSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                color = TextDim,
                fontSize = 8.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = TextLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = unit,
                color = NeonCyan.copy(alpha = 0.85f),
                fontSize = 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        }
    }
}
