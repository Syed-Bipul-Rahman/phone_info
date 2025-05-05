import 'package:flutter/material.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:phone_info/phone_info.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Map<String, dynamic>? _phoneInfo;
  Map<String, dynamic>? _batteryInfo;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _requestPermissions();
    _loadData();
    // Listen for battery updates
    PhoneInfoPlugin.batteryInfoStream.listen((batteryData) {
      setState(() {
        _batteryInfo = batteryData;
      });
    });
  }

  Future<void> _requestPermissions() async {
    Map<Permission, PermissionStatus> statuses =
        await [
          Permission.phone,
          Permission.camera,
          Permission.storage,
        ].request();
    if (await Permission.manageExternalStorage.isDenied) {
      await Permission.manageExternalStorage.request();
    }
    //  await openAppSettings();
  }

  Future<void> _loadData() async {
    try {
      final phoneInfo = await PhoneInfoPlugin.getPhoneInfo();
      setState(() {
        _phoneInfo = phoneInfo;
        _batteryInfo = phoneInfo['batteryInfo'] as Map<String, dynamic>?;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      if (mounted) {
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('Error loading phone info: $e')));
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('Phone Info Plugin Example')),
        body:
            _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _phoneInfo == null
                ? const Center(child: Text('Failed to load phone information'))
                : _buildInfoList(context),
      ),
    );
  }

  Widget _buildInfoList(BuildContext context) {
    return ListView(
      padding: const EdgeInsets.all(16.0),
      children: [
        _buildSection('Device Info', {
          'Name': _phoneInfo?['deviceName'],
          'Manufacturer': _phoneInfo?['manufacturer'],
          'Model': _phoneInfo?['model'],
          'OS Version': _phoneInfo?['osVersion'],
          'Architecture': _phoneInfo?['architecture'],
          'Device ID': _phoneInfo?['deviceId'],
        }),
        _buildSection('Battery Info', {
          'Level': '${_batteryInfo?['batteryLevel'] ?? 'N/A'}%',
          'Charging': _batteryInfo?['isCharging'] == true ? 'Yes' : 'No',
          'Health': _batteryInfo?['health'] ?? 'N/A',
          'Temperature': '${_batteryInfo?['temperature'] ?? 'N/A'}°C',
        }),
        _buildSection('Network Info', {
          'Connected': _phoneInfo?['isConnected'] == true ? 'Yes' : 'No',
          'Connection Type': _phoneInfo?['connectionType'] ?? 'N/A',
          'Network Operator': _phoneInfo?['networkOperator'] ?? 'N/A',
          'Signal Strength': _phoneInfo?['signalStrength']?.toString() ?? 'N/A',
        }),
        _buildSection('Storage & Memory', {
          'Total Memory': '${_phoneInfo?['totalMemory'] ?? 'N/A'} MB',
          'Available Storage': '${_phoneInfo?['availableStorage'] ?? 'N/A'} MB',
        }),
        ElevatedButton(
          onPressed: () async {
            try {
              final sensors = await PhoneInfoPlugin.getSensorInfo();
              _showModal(
                context,
                'Sensors',
                sensors.map((s) => '${s['name']} (${s['vendor']})').toList(),
              );
            } catch (e) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Error loading sensors: $e')),
              );
            }
          },
          child: const Text('View Sensors'),
        ),
        const SizedBox(height: 8),
        ElevatedButton(
          onPressed: () async {
            try {
              final cameras = await PhoneInfoPlugin.getCameraInfo();
              _showModal(
                context,
                'Cameras',
                cameras
                    .map(
                      (c) =>
                          '${c['isFront'] == true ? 'Front' : 'Rear'}: ${c['megapixels'] ?? 'N/A'} MP${c['hasFlash'] == true ? ' (with flash)' : ''}',
                    )
                    .toList(),
              );
            } catch (e) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Error loading cameras: $e')),
              );
            }
          },
          child: const Text('View Cameras'),
        ),
        const SizedBox(height: 8),
        ElevatedButton(
          onPressed: () async {
            try {
              final security = await PhoneInfoPlugin.getSecurityInfo();
              _showModal(context, 'Security Info', [
                'Rooted: ${security['isRooted'] == true ? 'Yes' : 'No'}',
                'Encrypted: ${security['isEncrypted'] == true ? 'Yes' : 'No'}',
              ]);
            } catch (e) {
              ScaffoldMessenger.of(context).showSnackBar(
                SnackBar(content: Text('Error loading security info: $e')),
              );
            }
          },
          child: const Text('View Security Info'),
        ),
      ],
    );
  }

  Widget _buildSection(String title, Map<String, dynamic> items) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16.0),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const Divider(),
            ...items.entries.map(
              (entry) => Padding(
                padding: const EdgeInsets.symmetric(vertical: 4.0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(
                      entry.key,
                      style: const TextStyle(fontWeight: FontWeight.w500),
                    ),
                    Text(
                      entry.value?.toString() ?? 'N/A',
                      style: TextStyle(color: Colors.grey[700]),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _showModal(BuildContext context, String title, List<String> items) {
    showModalBottomSheet(
      context: context,
      builder:
          (BuildContext modalContext) => Container(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const Divider(),
                Expanded(
                  child: ListView.builder(
                    itemCount: items.length,
                    itemBuilder: (context, index) {
                      return Padding(
                        padding: const EdgeInsets.symmetric(vertical: 4.0),
                        child: Text(items[index]),
                      );
                    },
                  ),
                ),
              ],
            ),
          ),
    );
  }
}
