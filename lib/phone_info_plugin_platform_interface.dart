import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'phone_info_plugin_method_channel.dart';

abstract class PhoneInfoPluginPlatform extends PlatformInterface {
  /// Constructs a PhoneInfoPluginPlatform.
  PhoneInfoPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static PhoneInfoPluginPlatform _instance = MethodChannelPhoneInfoPlugin();

  /// The default instance of [PhoneInfoPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelPhoneInfoPlugin].
  static PhoneInfoPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PhoneInfoPluginPlatform] when
  /// they register themselves.
  static set instance(PhoneInfoPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
