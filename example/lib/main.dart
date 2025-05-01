import 'package:flutter/material.dart';
import 'package:phone_info/phone_info.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
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
              print('Phone Info: $info');
              final version = await PhoneInfoPlugin.getPlatformVersion();
              print('Platform Version: $version');
              final devicename = await PhoneInfoPlugin.getDeviceId();
              print("device name $devicename");
              final storage = await PhoneInfoPlugin.getAvailableStorage();
              print("Storage $storage");
            },
            child: Text('Get Phone Info'),
          ),
        ),
      ),
    );
  }
}
