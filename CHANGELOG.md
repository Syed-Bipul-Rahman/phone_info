# Changelog

All notable changes to the `phone_info` will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2025-05-05

### Added
- Extensive new features beyond the initial release:
  - **Battery Information**:
    - Battery level, charging status, health, and temperature via `getBatteryInfo`.
    - Real-time battery updates through `batteryInfoStream`.
  - **CPU Information**:
    - Core count, maximum frequency, and load average via `getCpuInfo`.
  - **Screen Details**:
    - Resolution, density, refresh rate, and HDR support via `getScreenInfo`.
  - **Sensor Information**:
    - Complete list of device sensors with detailed specifications via `getSensorInfo`.
  - **Storage Information**:
    - Internal and external storage details via `getStorageInfo`.
  - **Camera Specifications**:
    - Detailed camera information (front/rear, megapixels, flash) via `getCameraInfo`.
  - **Biometric Features**:
    - Fingerprint, face recognition support via `getBiometricInfo`.
  - **SIM Card Details**:
    - Carrier information, network type, roaming status via `getSimInfo`.
  - **System Health**:
    - System temperature and status information via `getSystemHealth`.
  - **Accessibility Information**:
    - List of installed accessibility services via `getAccessibilityInfo`.
  - **Security Features**:
    - Root detection and encryption status via `getSecurityInfo`.
  - **App Usage Statistics**:
    - Recent app usage information via `getAppUsageStats`.
- Added event channel support for streaming real-time data.
- Enhanced error handling and permission management.

### Changed
- Improved comprehensive data retrieval with `getPhoneInfo` to include all new metrics.
- Updated documentation to reflect new functionality.

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