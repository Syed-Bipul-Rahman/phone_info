import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:phone_info/phone_info.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('Phone Info Plugin')),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {
              //give permission if required
              //   await Permission.phone.request();
              final info = await PhoneInfoPlugin.getPhoneInfo();
              final version = await PhoneInfoPlugin.getPlatformVersion();
              final deviceName = await PhoneInfoPlugin.getDeviceId();
              final storage = await PhoneInfoPlugin.getAvailableStorage();

              if (kDebugMode) {
                print('$info , $version , $deviceName , $storage');
              }
            },
            child: Text('Get Phone Info'),
          ),
        ),
      ),
    );
  }
}
