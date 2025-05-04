package me.bipul.phone_info.phone_info_plugin

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.ActivityManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.accessibility.AccessibilityManager
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.concurrent.TimeUnit

/** PhoneInfoPlugin */
class PhoneInfoPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var context: Context
    private var batteryEventSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "phone_info_plugin")
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "phone_info_plugin/battery_stream")
        channel.setMethodCallHandler(this)
        eventChannel.setStreamHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        try {
            when (call.method) {
                "getPhoneInfo" -> result.success(getPhoneInfo())
                "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
                "getIsConnected" -> result.success(isConnected())
                "getConnectionType" -> result.success(getConnectionType())
                "getNetworkOperator" -> result.success(getNetworkOperator())
                "getSignalStrength" -> result.success(getSignalStrength())
                "getDeviceName" -> result.success(getDeviceName())
                "getManufacturer" -> result.success(Build.MANUFACTURER)
                "getModel" -> result.success(Build.MODEL)
                "getOsVersion" -> result.success(Build.VERSION.RELEASE)
                "getArchitecture" -> result.success(System.getProperty("os.arch"))
                "getDeviceId" -> result.success(getDeviceId())
                "getTotalMemory" -> result.success(getTotalMemory())
                "getAvailableStorage" -> result.success(getAvailableStorage())
                "getBatteryInfo" -> result.success(getBatteryInfo())
                "getCpuInfo" -> result.success(getCpuInfo())
                "getNetworkInfo" -> result.success(getNetworkInfo())
                "getScreenInfo" -> result.success(getScreenInfo())
                "getSensorInfo" -> result.success(getSensorInfo())
                "getStorageInfo" -> result.success(getStorageInfo())
                "getCameraInfo" -> result.success(getCameraInfo())
                "getBiometricInfo" -> result.success(getBiometricInfo())
                "getSimInfo" -> result.success(getSimInfo())
                "getSystemHealth" -> result.success(getSystemHealth())
                "getAccessibilityInfo" -> result.success(getAccessibilityInfo())
                "getSecurityInfo" -> result.success(getSecurityInfo())
                "getAppUsageStats" -> result.success(getAppUsageStats())
                else -> result.notImplemented()
            }
        } catch (e: SecurityException) {
            result.error("PERMISSION_DENIED", "Permission denied: ${e.message}", null)
        } catch (e: Exception) {
            result.error("UNKNOWN_ERROR", "An error occurred: ${e.message}", null)
        }
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        batteryEventSink = events
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, intentFilter)
    }

    override fun onCancel(arguments: Any?) {
        context.unregisterReceiver(batteryReceiver)
        batteryEventSink = null
    }

    private val batteryReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val batteryInfo = getBatteryInfo()
            batteryEventSink?.success(batteryInfo)
        }
    }

    private fun getPhoneInfo(): Map<String, Any?> {
        return mapOf(
            "isConnected" to isConnected(),
            "connectionType" to getConnectionType(),
            "networkOperator" to getNetworkOperator(),
            "signalStrength" to getSignalStrength(),
            "deviceName" to getDeviceName(),
            "manufacturer" to Build.MANUFACTURER,
            "model" to Build.MODEL,
            "osVersion" to Build.VERSION.RELEASE,
            "architecture" to System.getProperty("os.arch"),
            "deviceId" to getDeviceId(),
            "totalMemory" to getTotalMemory(),
            "availableStorage" to getAvailableStorage(),
            "batteryInfo" to getBatteryInfo(),
            "cpuInfo" to getCpuInfo(),
            "networkInfo" to getNetworkInfo(),
            "screenInfo" to getScreenInfo(),
            "sensorInfo" to getSensorInfo(),
            "storageInfo" to getStorageInfo(),
            "cameraInfo" to getCameraInfo(),
            "biometricInfo" to getBiometricInfo(),
            "simInfo" to getSimInfo(),
            "systemHealth" to getSystemHealth(),
            "accessibilityInfo" to getAccessibilityInfo(),
            "securityInfo" to getSecurityInfo(),
            "appUsageStats" to getAppUsageStats()
        )
    }

    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        return network != null
    }

    private fun getConnectionType(): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return when {
            networkInfo == null || !networkInfo.isConnected -> "None"
            networkInfo.type == ConnectivityManager.TYPE_WIFI -> "Wi-Fi"
            networkInfo.type == ConnectivityManager.TYPE_MOBILE -> "Mobile Data"
            else -> "Unknown"
        }
    }

    private fun getNetworkOperator(): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try {
            tm.networkOperatorName
        } catch (e: SecurityException) {
            null
        }
    }

    private fun getSignalStrength(): Int? {
        return try {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.isWifiEnabled) {
                wifiManager.connectionInfo.rssi
            } else {
                null
            }
        } catch (e: SecurityException) {
            null
        }
    }

    private fun getDeviceName(): String? {
        return Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
            ?: Build.DEVICE
    }

    private fun getDeviceId(): String? {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            null
        }
    }

    private fun getTotalMemory(): Long {
        return Runtime.getRuntime().totalMemory() / (1024 * 1024) // MB
    }

    private fun getAvailableStorage(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        return stat.availableBytes / (1024 * 1024) // MB
    }

    private fun getBatteryInfo(): Map<String, Any?> {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            context.registerReceiver(null, ifilter)
        }

        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct: Float = if (level != -1 && scale != -1) level / scale.toFloat() * 100 else -1f
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        val health: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
        val temperature: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1

        return mapOf(
            "batteryLevel" to batteryPct.toInt(),
            "isCharging" to isCharging,
            "health" to when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
                else -> "Unknown"
            },
            "temperature" to (temperature / 10.0) // Convert to Celsius
        )
    }

    private fun getCpuInfo(): Map<String, Any?> {
        val coreCount = Runtime.getRuntime().availableProcessors()
        val maxFreq = try {
            File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").readText().trim().toLong() / 1000 // MHz
        } catch (e: Exception) {
            -1L
        }
        val loadAvg = try {
            val load = File("/proc/loadavg").readText().trim().split(" ")[0].toFloat()
            load
        } catch (e: Exception) {
            -1f
        }

        return mapOf(
            "coreCount" to coreCount,
            "maxFrequency" to maxFreq,
            "loadAverage" to loadAvg
        )
    }

    private fun getNetworkInfo(): Map<String, Any?> {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val localIp = try {
            NetworkInterface.getNetworkInterfaces().toList().flatMap { it.inetAddresses.toList() }
                .filterIsInstance<Inet4Address>().firstOrNull()?.hostAddress
        } catch (e: Exception) {
            null
        }
        val dns = try {
            wifiManager.dhcpInfo?.dns1?.toString() ?: "Unknown"
        } catch (e: Exception) {
            null
        }
        val networkType = try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when (tm.dataNetworkType) {
                TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                else -> "Unknown"
            }
        } catch (e: SecurityException) {
            "Unknown"
        }

        return mapOf(
            "localIp" to localIp,
            "dns" to dns,
            "networkType" to networkType
        )
    }

    private fun getScreenInfo(): Map<String, Any?> {
        val metrics = Resources.getSystem().displayMetrics
        val refreshRate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(android.view.WindowManager::class.java).defaultDisplay.refreshRate
        } else {
            -1f
        }
        val hdrSupport = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.getSystemService(android.view.WindowManager::class.java).defaultDisplay.isHdr
        } else {
            false
        }
        val notchPresent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context.getSystemService(android.view.WindowManager::class.java).defaultDisplay.cutout != null
        } else {
            false
        }

        return mapOf(
            "resolution" to "${metrics.widthPixels}x${metrics.heightPixels}",
            "density" to metrics.density,
            "refreshRate" to refreshRate,
            "hdrSupport" to hdrSupport,
            "notchPresent" to notchPresent
        )
    }

    private fun getSensorInfo(): List<Map<String, Any?>> {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
        return sensors.map { sensor ->
            mapOf(
                "name" to sensor.name,
                "type" to sensor.type,
                "vendor" to sensor.vendor,
                "maxRange" to sensor.maximumRange,
                "resolution" to sensor.resolution
            )
        }
    }

    private fun getStorageInfo(): Map<String, Any?> {
        val stat = StatFs(Environment.getDataDirectory().path)
        val totalStorage = stat.totalBytes / (1024 * 1024) // MB
        val externalStorage = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val extStat = StatFs(Environment.getExternalStorageDirectory().path)
            extStat.availableBytes / (1024 * 1024) // MB
        } else {
            -1L
        }

        return mapOf(
            "totalStorage" to totalStorage,
            "externalStorageAvailable" to (externalStorage != -1L),
            "externalStorage" to externalStorage
        )
    }

    private fun getCameraInfo(): List<Map<String, Any?>> {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIds = cameraManager.cameraIdList
        return cameraIds.map { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val isFront = characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
            val megapixels = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)?.let {
                (it.width * it.height) / 1_000_000.0
            }
            val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false

            mapOf(
                "id" to id,
                "isFront" to isFront,
                "megapixels" to megapixels,
                "hasFlash" to hasFlash
            )
        }
    }

    private fun getBiometricInfo(): Map<String, Any?> {
        val biometricManager = BiometricManager.from(context)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        )

        return mapOf(
            "fingerprint" to (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS),
            "faceRecognition" to (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS),
            "irisScanner" to false // Android doesn't explicitly expose iris scanner
        )
    }

    private fun getSimInfo(): Map<String, Any?> {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try {
            mapOf(
                "carrierName" to tm.networkOperatorName,
                "simState" to when (tm.simState) {
                    TelephonyManager.SIM_STATE_READY -> "Ready"
                    TelephonyManager.SIM_STATE_ABSENT -> "Absent"
                    else -> "Unknown"
                },
                "networkType" to when (tm.dataNetworkType) {
                    TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
                    TelephonyManager.NETWORK_TYPE_NR -> "5G"
                    else -> "Unknown"
                },
                "isRoaming" to tm.isNetworkRoaming,
                "imei" to if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) null else tm.deviceId
            )
        } catch (e: SecurityException) {
            mapOf(
                "carrierName" to null,
                "simState" to null,
                "networkType" to null,
                "isRoaming" to null,
                "imei" to null
            )
        }
    }

    private fun getSystemHealth(): Map<String, Any?> {
        return mapOf(
            "cpuTemperature" to -1.0,
            "status" to "Temperature monitoring not supported"
        )
    }

    private fun getAccessibilityInfo(): List<String> {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        return am.installedAccessibilityServiceList.map { it.id }
    }

    private fun getSecurityInfo(): Map<String, Any?> {
        val isRooted = checkRoot()
        val isEncrypted = try {
            val dm = context.getSystemService(Context.DEVICE_POLICY_SERVICE)
            dm != null // Simplified check
        } catch (e: Exception) {
            false
        }

        return mapOf(
            "isRooted" to isRooted,
            "isEncrypted" to isEncrypted
        )
    }

    private fun checkRoot(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su"
        )
        return paths.any { File(it).exists() }
    }

    private fun getAppUsageStats(): List<Map<String, Any?>> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(1)
        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        return stats?.map { stat ->
            mapOf(
                "packageName" to stat.packageName,
                "totalTimeInForeground" to stat.totalTimeInForeground / 1000 // Seconds
            )
        } ?: emptyList()
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
    }
}