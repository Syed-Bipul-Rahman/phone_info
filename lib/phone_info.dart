import 'dart:async';
import 'package:flutter/services.dart';

class PhoneInfoPlugin {
  static const MethodChannel _channel = MethodChannel('phone_info_plugin');
  static const EventChannel _batteryChannel = EventChannel(
    'phone_info_plugin/battery_stream',
  );

  /// Get all phone information in a single call
  static Future<Map<String, dynamic>> getPhoneInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getPhoneInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get the platform version
  static Future<String?> getPlatformVersion() async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// Check if device is connected to internet
  static Future<bool> getIsConnected() async {
    final bool result = await _channel.invokeMethod('getIsConnected');
    return result;
  }

  /// Get current connection type (Wi-Fi, Mobile Data, None)
  static Future<String?> getConnectionType() async {
    final String? result = await _channel.invokeMethod('getConnectionType');
    return result;
  }

  /// Get network operator name
  static Future<String?> getNetworkOperator() async {
    final String? result = await _channel.invokeMethod('getNetworkOperator');
    return result;
  }

  /// Get signal strength in dBm (for Wi-Fi)
  static Future<int?> getSignalStrength() async {
    final int? result = await _channel.invokeMethod('getSignalStrength');
    return result;
  }

  /// Get device name
  static Future<String?> getDeviceName() async {
    final String? result = await _channel.invokeMethod('getDeviceName');
    return result;
  }

  /// Get device manufacturer
  static Future<String?> getManufacturer() async {
    final String? result = await _channel.invokeMethod('getManufacturer');
    return result;
  }

  /// Get device model
  static Future<String?> getModel() async {
    final String? result = await _channel.invokeMethod('getModel');
    return result;
  }

  /// Get OS version
  static Future<String?> getOsVersion() async {
    final String? result = await _channel.invokeMethod('getOsVersion');
    return result;
  }

  /// Get device architecture
  static Future<String?> getArchitecture() async {
    final String? result = await _channel.invokeMethod('getArchitecture');
    return result;
  }

  /// Get device ID (Android ID)
  static Future<String?> getDeviceId() async {
    final String? result = await _channel.invokeMethod('getDeviceId');
    return result;
  }

  /// Get total memory in MB
  static Future<int?> getTotalMemory() async {
    final int? result = await _channel.invokeMethod('getTotalMemory');
    return result;
  }

  /// Get available storage in MB
  static Future<int?> getAvailableStorage() async {
    final int? result = await _channel.invokeMethod('getAvailableStorage');
    return result;
  }

  /// Get battery information
  static Future<Map<String, dynamic>> getBatteryInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getBatteryInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get CPU information
  static Future<Map<String, dynamic>> getCpuInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getCpuInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get network information
  static Future<Map<String, dynamic>> getNetworkInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getNetworkInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get screen information
  static Future<Map<String, dynamic>> getScreenInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getScreenInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get sensor information
  static Future<List<Map<String, dynamic>>> getSensorInfo() async {
    final List<dynamic> result = await _channel.invokeMethod('getSensorInfo');
    return result.map((item) => Map<String, dynamic>.from(item)).toList();
  }

  /// Get storage information
  static Future<Map<String, dynamic>> getStorageInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getStorageInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get camera information
  static Future<List<Map<String, dynamic>>> getCameraInfo() async {
    final List<dynamic> result = await _channel.invokeMethod('getCameraInfo');
    return result.map((item) => Map<String, dynamic>.from(item)).toList();
  }

  /// Get biometric information
  static Future<Map<String, dynamic>> getBiometricInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getBiometricInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get SIM information
  static Future<Map<String, dynamic>> getSimInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getSimInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get system health information
  static Future<Map<String, dynamic>> getSystemHealth() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getSystemHealth',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get accessibility information
  static Future<List<String>> getAccessibilityInfo() async {
    final List<dynamic> result = await _channel.invokeMethod(
      'getAccessibilityInfo',
    );
    return result.cast<String>();
  }

  /// Get security information
  static Future<Map<String, dynamic>> getSecurityInfo() async {
    final Map<dynamic, dynamic> result = await _channel.invokeMethod(
      'getSecurityInfo',
    );
    return Map<String, dynamic>.from(result);
  }

  /// Get app usage statistics
  static Future<List<Map<String, dynamic>>> getAppUsageStats() async {
    final List<dynamic> result = await _channel.invokeMethod(
      'getAppUsageStats',
    );
    return result.map((item) => Map<String, dynamic>.from(item)).toList();
  }

  /// Stream to listen for battery changes
  static Stream<Map<String, dynamic>> get batteryInfoStream {
    return _batteryChannel.receiveBroadcastStream().map(
      (dynamic event) => Map<String, dynamic>.from(event),
    );
  }
}
