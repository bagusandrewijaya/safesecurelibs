# SafeSecureLibs

A Flutter package for checking device security status including root detection, developer mode, and dangerous apps detection on Android devices.

## Features

- Developer Mode Detection: Check if developer options are enabled
- Root Access Detection: Detect if the device is rooted
- Magisk Detection: Identify Magisk root management installation
- Dangerous Apps Detection: Detect potentially harmful apps installed on the device

## Installation

Add this to your package's `pubspec.yaml` file:```yaml
dependencies:
  safe_secure_libs: ^1.0.0
```

Then run:
```bash
flutter pub get
```

## Usage

1. Import the package:
```dart
import 'package:safe_secure_libs/safe_secure_libs.dart';
```

2. Use the security checker:
```dart
final securityStatus = await CheckerMethode.checkSecurityStatus();

// Access individual security checks
bool isDevModeEnabled = securityStatus['isDevModeEnabled'];
bool isRooted = securityStatus['isRooted'];
bool isMagiskDetected = securityStatus['isMagiskDetected'];
bool hasDangerousApps = securityStatus['hasDangerousApps'];
```

3. Example implementation:
```dart
class SecurityCheckPage extends StatefulWidget {
  @override
  State createState() => _SecurityCheckPageState();
}

class _SecurityCheckPageState extends State {
  Map _securityStatus = {
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

  Future _checkSecurity() async {
    final status = await CheckerMethode.checkSecurityStatus();
    setState(() {
      _securityStatus = status;
    });
  }
}
`


## Platform Support

| Android | iOS |
|:-------:|:---:|
|    ✅    |  ❌  |

## Configuration

### Android

No additional configuration required. The package automatically handles all necessary Android-specific implementations.

### iOS

iOS support is currently not available.

## Security Checks Details

### Developer Mode Detection
Checks if developer options are enabled in the device settings. This can indicate potential security risks as developer options may enable USB debugging and other sensitive features.

### Root Detection
Performs various checks to determine if the device has been rooted, including:
- Checking for common root management apps
- Scanning for su binary in various system locations
- Verifying system partition mount status

### Magisk Detection
Specifical

