# Changelog

All notable changes to the `phone_info` will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/0.0.1/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.1] - 2025-05-01

### Added
- Initial release of the `phone_info` for Android.
- **Network Information**:
    - Check network connection status (`getIsConnected`).
    - Retrieve connection type (`Wi-Fi`, `Mobile Data`, `None`) via `getConnectionType`.
    - Get network operator name (`getNetworkOperator`, requires `READ_PHONE_STATE` permission).
    - Fetch Wi-Fi signal strength in RSSI (dBm) via `getSignalStrength`.
- **Device Information**:
    - Device name (`getDeviceName`).
    - Manufacturer (`getManufacturer`).
    - Device model (`getModel`).
    - Operating system version (`getOsVersion`).
    - Device architecture (`getArchitecture`).
    - Unique device ID (`getDeviceId`).
    - Total memory in MB (`getTotalMemory`).
    - Available storage in MB (`getAvailableStorage`).
- Flexible API with `getPhoneInfo` to retrieve all information in a single call.
- Example app demonstrating usage of all methods.
- Support for Android API 21+ (minSdk 21).
- Error handling for permission denials and unavailable data.
- Unit tests for Dart and Kotlin code.
- Comprehensive documentation in `README.md`.

### Notes
- iOS support is not included in this release. Contributions for iOS implementation are welcome.
- Requires Android permissions: `ACCESS_WIFI_STATE`, `ACCESS_NETWORK_STATE`, and `READ_PHONE_STATE` (runtime permission for the latter).
