package me.bipul.phone_info.phone_info_plugin

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** PhoneInfoPlugin */
class PhoneInfoPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "phone_info_plugin")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
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
            else -> result.notImplemented()
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
            "availableStorage" to getAvailableStorage()
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

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}