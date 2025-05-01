# Phone Info

A Flutter plugin for retrieving detailed phone information on Android devices, including network
status, device details, and system specifications.
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
- **Device Information**:
    - Device name (e.g., "realme C61").
    - Manufacturer (e.g., "realme").
    - Model (e.g., "RMX3930").
    - Operating system version (e.g., "14").
    - Architecture (e.g., "aarch64").
    - Unique device ID.
    - Total memory (RAM) in MB.
    - Available storage in MB.
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
  phone_info: ^0.0.1
```

Run the following command to install the plugin:

```bash
flutter pub get
```

### Android Setup

1. **Permissions**: Add the following permissions to your
   app’s `android/app/src/main/AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
   <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
   ```

2. **Runtime Permissions**: For `READ_PHONE_STATE` (required for `getNetworkOperator`), request
   permission at runtime using a package like `permission_handler` (see example below).

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
                  final deviceId = await PhoneInfoPlugin.getDeviceId();
                  setState(() {
                    _output = 'Device ID: $deviceId';
                  });
                } catch (e) {
                  setState(() {
                    _output = 'Error: $e';
                  });
                }
              },
              child: const Text('Get Device ID'),
            ),
          ],
        ),
      ),
    );
  }
}
```

## Complete Example

For a complete see the [example app](example/lib/main.dart).

### Example Output

When you press the buttons, you’ll see output like:

```
All Info: {isConnected: true, connectionType: Wi-Fi, networkOperator: Robi, signalStrength: -32, deviceName: realme C61, manufacturer: realme, model: RMX3930, osVersion: 14, architecture: aarch64, deviceId: d55d784905cd7b6e, totalMemory: 26, availableStorage: 86325}
Device Name: realme C61
Device ID: d55d784905cd7b6e
```

## API Reference

The `PhoneInfoPlugin` class provides the following methods:

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

### Requesting Runtime Permissions

Use the `permission_handler` package to request `READ_PHONE_STATE` at runtime:

```dart
import 'package:permission_handler/permission_handler.dart';

Future<void> requestPermissions() async {
  if (await Permission.phone
      .request()
      .isGranted) {
    // Permission granted
  } else {
    // Handle permission denial
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