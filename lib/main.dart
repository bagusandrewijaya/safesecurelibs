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
  Map<String, bool> _securityStatus = {
    'isDevModeEnabled': false,
    'isRooted': false,
    'isMagiskDetected': false,
    'hasDangerousApps': false
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
          const SizedBox(height: 20),
          ElevatedButton(
            onPressed: _checkSecurity,
            child: const Text('Refresh Security Status'),
          ),
        ],
      ),
    );
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
}
