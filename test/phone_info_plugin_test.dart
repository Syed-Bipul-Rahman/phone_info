import 'package:flutter_test/flutter_test.dart';
import 'package:phone_info/phone_info.dart';
import 'package:phone_info/phone_info_plugin_method_channel.dart';
import 'package:phone_info/phone_info_plugin_platform_interface.dart';

import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPhoneInfoPluginPlatform
    with MockPlatformInterfaceMixin
    implements PhoneInfoPluginPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final PhoneInfoPluginPlatform initialPlatform =
      PhoneInfoPluginPlatform.instance;

  test('$MethodChannelPhoneInfoPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPhoneInfoPlugin>());
  });

  test('getPlatformVersion', () async {
    PhoneInfoPlugin phoneInfoPlugin = PhoneInfoPlugin();
    MockPhoneInfoPluginPlatform fakePlatform = MockPhoneInfoPluginPlatform();
    PhoneInfoPluginPlatform.instance = fakePlatform;

    expect(await PhoneInfoPlugin.getPlatformVersion(), '42');
  });
}
