import 'package:flutter/services.dart';

class PhoneInfoPlugin {
  static const MethodChannel _channel = MethodChannel('phone_info_plugin');

  /// Get all phone information in a single call
  static Future<Map<String, dynamic>?> getPhoneInfo() async {
    final Map<dynamic, dynamic>? result = await _channel.invokeMethod(
      'getPhoneInfo',
    );
    return result?.cast<String, dynamic>();
  }

  /// Get the Android platform version
  static Future<String?> getPlatformVersion() async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// Check if the device is connected to a network
  static Future<bool?> getIsConnected() async {
    return await _channel.invokeMethod('getIsConnected');
  }

  /// Get the current network connection type (Wi-Fi, Mobile Data, None)
  static Future<String?> getConnectionType() async {
    return await _channel.invokeMethod('getConnectionType');
  }

  /// Get the network operator name (requires READ_PHONE_STATE permission)
  static Future<String?> getNetworkOperator() async {
    return await _channel.invokeMethod('getNetworkOperator');
  }

  /// Get the Wi-Fi signal strength in RSSI (dBm)
  static Future<int?> getSignalStrength() async {
    return await _channel.invokeMethod('getSignalStrength');
  }

  /// Get the device name
  static Future<String?> getDeviceName() async {
    return await _channel.invokeMethod('getDeviceName');
  }

  /// Get the device manufacturer
  static Future<String?> getManufacturer() async {
    return await _channel.invokeMethod('getManufacturer');
  }

  /// Get the device model
  static Future<String?> getModel() async {
    return await _channel.invokeMethod('getModel');
  }

  /// Get the operating system version
  static Future<String?> getOsVersion() async {
    return await _channel.invokeMethod('getOsVersion');
  }

  /// Get the device architecture (e.g., arm64-v8a)
  static Future<String?> getArchitecture() async {
    return await _channel.invokeMethod('getArchitecture');
  }

  /// Get the unique device ID
  static Future<String?> getDeviceId() async {
    return await _channel.invokeMethod('getDeviceId');
  }

  /// Get the total memory in MB
  static Future<int?> getTotalMemory() async {
    return await _channel.invokeMethod('getTotalMemory');
  }

  /// Get the available storage in MB
  static Future<int?> getAvailableStorage() async {
    return await _channel.invokeMethod('getAvailableStorage');
  }
}
