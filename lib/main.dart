import 'package:flutter/material.dart';
import 'checkermethode.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Security Check Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const SecurityCheckPage(),
    );
  }
}

class SecurityCheckPage extends StatefulWidget {
  const SecurityCheckPage({super.key});
  @override
  State<SecurityCheckPage> createState() => _SecurityCheckPageState();
}
class _SecurityCheckPageState extends State<SecurityCheckPage> {
  Map<String, dynamic> _securityStatus = {
    'isDevModeEnabled': false,
    'isRooted': false,
    'isMagiskDetected': false,
    'hasDangerousApps': false,
    'isSecure': true,
    'deviceInfo': {}
  };

  @override
  void initState() {
    super.initState();
    _checkSecurity();
  }

  Future<void> _checkSecurity() async {
    final status = await CheckerMethode.checkSecurityStatus();
    setState(() {
      _securityStatus = status;
    });
  }

  @override
  Widget build(BuildContext context) {
    final deviceInfo = _securityStatus['deviceInfo'] as Map<dynamic, dynamic>? ?? {};
    
    return Scaffold(
      appBar: AppBar(
        title: const Text('Security Check'),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          _buildSecurityItem(
            'Developer Mode',
            _securityStatus['isDevModeEnabled'] ?? false,
          ),
          _buildSecurityItem(
            'Root Access',
            _securityStatus['isRooted'] ?? false,
          ),
          _buildSecurityItem(
            'Magisk Detected',
            _securityStatus['isMagiskDetected'] ?? false,
          ),
          _buildSecurityItem(
            'Dangerous Apps',
            _securityStatus['hasDangerousApps'] ?? false,
          ),
          _buildSecurityItem(
            'Device Security',
            _securityStatus['isSecure'] ?? true,
          ),
          const SizedBox(height: 20),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Device Information',
                      style: Theme.of(context).textTheme.titleLarge),
                  const SizedBox(height: 8),
                  Text('Model: ${deviceInfo['model']}'),
                  Text('Brand: ${deviceInfo['brand']}'),
                  Text('Android Version: ${deviceInfo['version']}'),
                  Text('SDK: ${deviceInfo['sdkInt']}'),
                ],
              ),
            ),
          ),
          const SizedBox(height: 20),
          ElevatedButton(
            onPressed: _checkSecurity,
            child: const Text('Refresh Security Status'),
          ),
        ],
      ),
    );
  }
}

  Widget _buildSecurityItem(String title, bool isDetected) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8.0),
      child: ListTile(
        title: Text(title),
        trailing: Icon(
          isDetected ? Icons.warning : Icons.check_circle,
          color: isDetected ? Colors.red : Colors.green,
        ),
        subtitle: Text(
          isDetected ? 'Security Risk Detected' : 'Secure',
          style: TextStyle(
            color: isDetected ? Colors.red : Colors.green,
          ),
        ),
      ),
    );
  }

