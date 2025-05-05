# Phone Info

A Flutter plugin for retrieving detailed phone information on Android devices, including network
status, device details, battery information, sensors, and comprehensive system specifications.
This plugin provides a flexible API to fetch all information at once or individual pieces as needed,
making it ideal for apps requiring device or network insights.

[![Pub Version](https://img.shields.io/pub/v/phone_info.svg)](https://pub.dev/packages/phone_info)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Features

- **Network Information**:
  - Check if the device is connected to a network (`isConnected`).
  - Get the connection type (`Wi-Fi`, `Mobile Data`, or `None`).
  - Retrieve the network operator name (e.g., "Robi").
  - Get Wi-Fi signal strength in RSSI (dBm).
  - Access detailed network data (local IP, DNS, network type).
- **Device Information**:
  - Device name (e.g., "realme C61").
  - Manufacturer (e.g., "realme").
  - Model (e.g., "RMX3930").
  - Operating system version (e.g., "14").
  - Architecture (e.g., "aarch64").
  - Unique device ID.
  - Total memory (RAM) in MB.
  - Available storage in MB.
- **Battery Information**:
  - Battery level percentage.
  - Charging status.
  - Battery health.
  - Temperature.
  - Real-time battery status updates via event streams.
- **Hardware Information**:
  - CPU details (core count, frequency, load average).
  - Screen specifications (resolution, density, refresh rate, HDR support).
  - Sensor list with detailed information.
  - Storage details (internal and external).
  - Camera specifications.
  - Biometric capabilities.
- **System Information**:
  - SIM card details.
  - System health metrics.
  - Accessibility services.
  - Security information (root detection, encryption status).
  - App usage statistics.
- **Flexible API**: Fetch all information in a single call or retrieve individual pieces as needed.
- **Error Handling**: Gracefully handles permission denials and unavailable data.

## Platform Support

| Platform | Supported | Notes                                                     |
|----------|-----------|-----------------------------------------------------------|
| Android  | ✅         | Fully supported (API 21+).                                |
| iOS      | ❌         | iOS support not yet implemented (contributions welcome!). |

## Installation

Add the `phone_info` to your `pubspec.yaml`:

```yaml
dependencies:
  phone_info: ^0.1.0
```

Run the following command to install the plugin:

```bash
flutter pub get
```

### Android Setup

1. **Permissions**: Add the following permissions to your
   app's `android/app/src/main/AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
   <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
   ```

2. **Runtime Permissions**: For certain features, request permissions at runtime using a package like `permission_handler`:
  - `READ_PHONE_STATE` for network operator information
  - `PACKAGE_USAGE_STATS` for app usage statistics (requires special approval from user)

## Usage

### Basic Example

Below is a simple Flutter app demonstrating how to use the plugin to fetch phone information:

```dart
import 'package:flutter/material.dart';
import 'package:phone_info/phone_info.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: const PhoneInfoScreen(),
    );
  }
}

class PhoneInfoScreen extends StatefulWidget {
  const PhoneInfoScreen({super.key});

  @override
  _PhoneInfoScreenState createState() => _PhoneInfoScreenState();
}

class _PhoneInfoScreenState extends State<PhoneInfoScreen> {
  String _output = 'Press a button to fetch info';

  Future<void> _requestPermissions() async {
    await Permission.phone.request();
  }

  @override
  void initState() {
    super.initState();
    _requestPermissions();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Phone Info Plugin')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(_output, style: const TextStyle(fontSize: 16)),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () async {
                try {
                  final info = await PhoneInfoPlugin.getPhoneInfo();
                  setState(() {
                    _output = 'All Info: $info';
                  });
                } catch (e) {
                  setState(() {
                    _output = 'Error: $e';
                  });
                }
              },
              child: const Text('Get All Phone Info'),
            ),
            ElevatedButton(
              onPressed: () async {
                try {
                  final deviceName = await PhoneInfoPlugin.getDeviceName();
                  setState(() {
                    _output = 'Device Name: $deviceName';
                  });
                } catch (e) {
                  setState(() {
                    _output = 'Error: $e';
                  });
                }
              },
              child: const Text('Get Device Name'),
            ),
            ElevatedButton(
              onPressed: () async {
                try {
                  final batteryInfo = await PhoneInfoPlugin.getBatteryInfo();
                  setState(() {
                    _output = 'Battery Info: $batteryInfo';
                  });
                } catch (e) {
                  setState(() {
                    _output = 'Error: $e';
                  });
                }
              },
              child: const Text('Get Battery Info'),
            ),
          ],
        ),
      ),
    );
  }
}
```

### Streaming Battery Updates

The plugin also supports streaming real-time battery updates:

```dart
import 'package:flutter/material.dart';
import 'package:phone_info/phone_info.dart';

class BatteryMonitorScreen extends StatefulWidget {
  const BatteryMonitorScreen({super.key});

  @override
  _BatteryMonitorScreenState createState() => _BatteryMonitorScreenState();
}

class _BatteryMonitorScreenState extends State<BatteryMonitorScreen> {
  Map<String, dynamic>? _batteryInfo;
  late Stream<Map<String, dynamic>> _batteryStream;

  @override
  void initState() {
    super.initState();
    _batteryStream = PhoneInfoPlugin.batteryInfoStream();
    _batteryStream.listen((batteryInfo) {
      setState(() {
        _batteryInfo = batteryInfo;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Battery Monitor')),
      body: Center(
        child: _batteryInfo == null
            ? const CircularProgressIndicator()
            : Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text('Battery Level: ${_batteryInfo!['batteryLevel']}%'),
                  Text('Status: ${_batteryInfo!['isCharging'] ? 'Charging' : 'Not Charging'}'),
                  Text('Health: ${_batteryInfo!['health']}'),
                  Text('Temperature: ${_batteryInfo!['temperature']}°C'),
                ],
              ),
      ),
    );
  }
}
```

## Complete Example

For a complete working example, see the [example app](example/lib/main.dart).

### Example Output

When you press the buttons, you'll see output like:

```
All Info: {isConnected: true, connectionType: Wi-Fi, networkOperator: Robi, signalStrength: -32, deviceName: realme C61, manufacturer: realme, model: RMX3930, osVersion: 14, architecture: aarch64, deviceId: d55d784905cd7b6e, totalMemory: 26, availableStorage: 86325, batteryInfo: {batteryLevel: 85, isCharging: true, health: Good, temperature: 30.2}, cpuInfo: {coreCount: 8, maxFrequency: 2400, loadAverage: 1.2}, ...}

Battery Info: {batteryLevel: 85, isCharging: true, health: Good, temperature: 30.2}
```

## API Reference

The `PhoneInfoPlugin` class provides the following methods:

### Basic Information

| Method                  | Return Type                     | Description                                                   |
|-------------------------|---------------------------------|---------------------------------------------------------------|
| `getPhoneInfo()`        | `Future<Map<String, dynamic>?>` | Retrieves all phone information in a single call.             |
| `getPlatformVersion()`  | `Future<String?>`               | Gets the Android OS version (e.g., "Android 14").             |
| `getIsConnected()`      | `Future<bool?>`                 | Checks if the device is connected to a network.               |
| `getConnectionType()`   | `Future<String?>`               | Gets the connection type (`Wi-Fi`, `Mobile Data`, `None`).    |
| `getNetworkOperator()`  | `Future<String?>`               | Gets the network operator name (requires `READ_PHONE_STATE`). |
| `getSignalStrength()`   | `Future<int?>`                  | Gets the Wi-Fi signal strength in RSSI (dBm).                 |
| `getDeviceName()`       | `Future<String?>`               | Gets the device name (e.g., "realme C61").                    |
| `getManufacturer()`     | `Future<String?>`               | Gets the manufacturer (e.g., "realme").                       |
| `getModel()`            | `Future<String?>`               | Gets the device model (e.g., "RMX3930").                      |
| `getOsVersion()`        | `Future<String?>`               | Gets the OS version (e.g., "14").                             |
| `getArchitecture()`     | `Future<String?>`               | Gets the device architecture (e.g., "aarch64").               |
| `getDeviceId()`         | `Future<String?>`               | Gets the unique device ID.                                    |
| `getTotalMemory()`      | `Future<int?>`                  | Gets the total memory in MB.                                  |
| `getAvailableStorage()` | `Future<int?>`                  | Gets the available storage in MB.                             |

### Advanced Information

| Method                  | Return Type                       | Description                                           |
|-------------------------|-----------------------------------|-------------------------------------------------------|
| `getBatteryInfo()`      | `Future<Map<String, dynamic>?>`   | Gets detailed battery information.                     |
| `getCpuInfo()`          | `Future<Map<String, dynamic>?>`   | Gets CPU details (cores, frequency, load).             |
| `getNetworkInfo()`      | `Future<Map<String, dynamic>?>`   | Gets detailed network information (IP, DNS, etc.).     |
| `getScreenInfo()`       | `Future<Map<String, dynamic>?>`   | Gets screen specifications.                            |
| `getSensorInfo()`       | `Future<List<Map<String, dynamic>>?>` | Gets list of device sensors with details.         |
| `getStorageInfo()`      | `Future<Map<String, dynamic>?>`   | Gets detailed storage information.                     |
| `getCameraInfo()`       | `Future<List<Map<String, dynamic>>?>` | Gets camera specifications.                       |
| `getBiometricInfo()`    | `Future<Map<String, dynamic>?>`   | Gets biometric capabilities.                           |
| `getSimInfo()`          | `Future<Map<String, dynamic>?>`   | Gets SIM card details.                                 |
| `getSystemHealth()`     | `Future<Map<String, dynamic>?>`   | Gets system health metrics.                            |
| `getAccessibilityInfo()` | `Future<List<String>?>`          | Gets installed accessibility services.                 |
| `getSecurityInfo()`     | `Future<Map<String, dynamic>?>`   | Gets security information (root detection, etc.).      |
| `getAppUsageStats()`    | `Future<List<Map<String, dynamic>>?>` | Gets app usage statistics (requires special permission). |

### Event Streams

| Method                  | Return Type                       | Description                                           |
|-------------------------|-----------------------------------|-------------------------------------------------------|
| `batteryInfoStream()`   | `Stream<Map<String, dynamic>>`    | Streams real-time battery updates.                     |

### Notes

- All methods are asynchronous and return `Future`s. Use `await` to get the result (
  e.g., `await PhoneInfoPlugin.getDeviceName()`).
- Methods that may fail due to permissions (e.g., `getNetworkOperator`) return `null` if the data is
  unavailable.
- Wrap method calls in `try-catch` blocks to handle potential platform exceptions.

## Permissions

The plugin requires the following Android permissions:

| Permission             | Purpose                         | Runtime Permission Required? |
|------------------------|---------------------------------|------------------------------|
| `ACCESS_WIFI_STATE`    | Access Wi-Fi signal strength    | No                           |
| `ACCESS_NETWORK_STATE` | Check network connection status | No                           |
| `READ_PHONE_STATE`     | Get network operator name       | Yes (Android 6.0+)           |
| `PACKAGE_USAGE_STATS`  | Get app usage statistics        | Yes (special approval)       |

### Requesting Runtime Permissions

Use the `permission_handler` package to request permissions at runtime:

```dart
import 'package:permission_handler/permission_handler.dart';

Future<void> requestPermissions() async {
  if (await Permission.phone.request().isGranted) {
    // Permission granted
  } else {
    // Handle permission denial
  }
  
  // For app usage stats (special permission)
  if (await Permission.systemAppInfo.request().isGranted) {
    // Permission granted
  } else {
    // Direct user to settings
    await openAppSettings();
  }
}
```

## Troubleshooting

- **MissingPluginException**: If you see `No implementation found for method ...`, ensure the plugin
  is properly built:
  - Run `flutter clean` and `flutter pub get` in both the plugin and app directories.
  - Rebuild the app with `flutter run`.
- **Instance of 'Future<String?>'**: Ensure you use `await` when calling asynchronous methods (
  e.g., `await PhoneInfoPlugin.getDeviceName()`).
- **Null Values**: If methods like `getNetworkOperator` return `null`, check that the required
  permissions are granted.
- **Build Failures**: Ensure your `android/build.gradle` includes `androidx.core:core-ktx:1.12.0`
  and the correct Kotlin version (e.g., `1.8.22` or later).
- **App Usage Stats Not Working**: This requires special permission. Guide users to enable it in settings:
    ```dart
    // Check if permission is granted
    if (!(await Permission.systemAppInfo.isGranted)) {
      // Open settings
      await openAppSettings();
    }
    ```

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m 'Add your feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

Please include tests and update the documentation for new features.

## License

This plugin is licensed under the [MIT License](LICENSE).

## Contact

For issues or feature requests, please open an issue on
the [GitHub repository](https://github.com/Syed-Bipul-Rahman/phone_info).