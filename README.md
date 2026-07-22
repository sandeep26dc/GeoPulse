<div align="center">

  <!-- Project Title & Subtitle -->
  <h1>G E O P U L S E</h1>
  <p><strong>Precision Android Location Telemetry Engine</strong></p>

  <!-- Badges -->
  <a href="https://github.com/your-username/GeoPulse/actions">
    <img src="https://img.shields.io/badge/Build-Passing-10B981?style=for-the-badge&logo=githubactions&logoColor=white" alt="Build Status">
  </a>
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  </a>
  <a href="https://developer.android.com/jetpack/compose">
    <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white" alt="Compose">
  </a>
  <a href="https://github.com/your-username/GeoPulse/releases">
    <img src="https://img.shields.io/badge/Android-API%2026%2B-000000?style=for-the-badge&logo=android&logoColor=3DDC84" alt="Android Version">
  </a>

  <br><br>

  <!-- High-Res App Preview / Screenshot -->
  <img src="docs/screenshots/dashboard_preview.png" width="340" alt="GeoPulse Luxury Dashboard" style="border-radius: 16px;">

  <br><br>

  <p width="80%">
    <em>GeoPulse is a lightweight, high-performance telemetry dashboard designed for Android. Engineered with an obsidian and titanium design language, it transforms standard location tracking into an executive instrument cluster.</em>
  </p>

  <a href="https://github.com/your-username/GeoPulse/releases">
    <img src="https://img.shields.io/badge/⚡_DOWNLOAD_DEBUG_APK-05070A?style=for-the-badge&logo=android&logoColor=white" alt="Download APK">
  </a>

</div>

---

## 🚀 Key Highlights

* **Obsidian & Titanium Aesthetic:** OLED-optimized dark UI designed for zero distraction and maximum clarity.
* **Fluid Dual-Ring Speed Gauge:** Real-time speed and heading bearing visualized with zero animation stutter.
* **High-Precision Fused Location Engine:** Powered by Google Play Services location APIs for rapid signal locking.
* **Zero Bloat:** Zero third-party ad networks, no cloud sync overhead, and ultra-low memory footprint.

---

## 🛠️ Architecture & Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Kotlin 2.0 |
| **UI Framework** | Jetpack Compose + Material3 |
| **Location Engine** | Google Play Services (`play-services-location`) |
| **Build System** | Gradle 8.7 (Kotlin DSL) |
| **CI/CD** | GitHub Actions Automated Compilation |

---

## 📸 Screenshots

<div align="center">
  <img src="docs/screenshots/main_screen.png" width="260" alt="Main Dashboard" />
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="docs/screenshots/metrics_detail.png" width="260" alt="Metrics View" />
</div>

---

## 📦 Local Installation & Building

```bash
# Clone the repository
git clone [https://github.com/your-username/GeoPulse.git](https://github.com/your-username/GeoPulse.git)

# Navigate into project directory
cd GeoPulse

# Assemble Debug APK
gradle assembleDebug
