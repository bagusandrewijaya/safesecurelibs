# SafeSecureLibs

<img src="https://raw.githubusercontent.com/bagusandrewijaya/safesecurelibs/refs/heads/main/asset/png.jpg" alt="SafeSecureLibs Banner" width="1000" height="400" />

A Flutter package for checking device security status including root detection, developer mode, and dangerous apps detection on Android devices.

## Features

- Developer Mode Detection: Check if developer options are enabled
- Root Access Detection: Detect if the device is rooted
- Magisk Detection: Identify Magisk root management installation
- Dangerous Apps Detection: Detect potentially harmful apps installed on the device

## Installation

Add this to your package's `pubspec.yaml` file:

```yaml
dependencies:
  safe_secure_libs: ^1.0.0
```

Then run:

```bash
flutter pub get
```

## Usage

Here's a complete example of how to implement the security checks in your Flutter app:

```dart
import 'package:flutter/material.dart';
import 'package:safesecurelibs/checkermethode.dart';

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
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Security Check'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
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
```

## Security Checks Details

### Developer Mode Detection
Checks if developer options are enabled in the device settings. This can indicate potential security risks as developer options may enable USB debugging and other sensitive features.

### Root Detection
Performs various checks to determine if the device has been rooted, including:
- Checking for common root management apps
- Scanning for su binary in various system locations
- Verifying system partition mount status

### Magisk Detection
Specifically checks for the presence of Magisk root management tool and related modifications to the system.

### Dangerous Apps Detection
Scans for the presence of potentially harmful applications, including:

- Root management apps
- System modification tools
- Security bypass applications

The following packages are detected:
```
com.topjohnwu.magisk
com.thirdparty.superuser
eu.chainfire.supersu
com.noshufou.android.su
com.koushikdutta.superuser
com.zachspong.temprootremovejb
com.ramdroid.appquarantine
com.formyhm.hideroot
com.amphoras.hidemyroot
com.saurik.substrate
de.robv.android.xposed
com.devadvance.rootcloak
com.devadvance.rootcloakplus
com.android.vending.billing.InAppBillingService.COIN
com.chelpus.lackypatch
com.kingroot.kinguser
com.kingo.root
com.smedialink.oneclickroot
com.zhiqupk.root.global
com.alephzain.framaroot
com.koushikdutta.rommanager
com.dimonvideo.luckypatcher
com.chelpus.luckypatcher
com.yellowes.su
com.koushikdutta.superuser
com.thirdparty.superuser
com.bitcnew.blockchain
com.noshufou.android.su.elite
com.jrummy.root.browserfree
com.jrummy.busybox.installer
```

## Platform Support

| Platform | Support |
|----------|---------|
| Android  | ✅      |
| iOS      | ❌      |

## Configuration

### Android
No additional configuration required. The package automatically handles all necessary Android-specific implementations.

### iOS
iOS support is currently not available.