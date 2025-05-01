import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'phone_info_plugin_platform_interface.dart';

/// An implementation of [PhoneInfoPluginPlatform] that uses method channels.
class MethodChannelPhoneInfoPlugin extends PhoneInfoPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('phone_info_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
