import 'package:flutter/services.dart';
import 'dart:io' show Platform;

class CheckerMethode {
  static const platform = MethodChannel('safesecurelibs');

 static Future<Map<String, dynamic>> checkSecurityStatus() async {
    if (!Platform.isAndroid) {
      return _getDefaultSecurityStatus();
    }

    try {
      // Check dev mode separately first
      final bool devMode = await platform.invokeMethod('isDevModeEnabled');
      print('Dev Mode Status: $devMode'); // Debug log

      final result = await platform.invokeMethod('checkSecurityStatus');
      print('Full Security Check Result: $result'); // Debug log

      // Override the dev mode status from the full check
      final Map<String, dynamic> status = Map<String, dynamic>.from(result);
      status['isDevModeEnabled'] = devMode;
      status['isSecure'] = !(devMode || 
                            status['isRooted'] == true || 
                            status['isMagiskDetected'] == true || 
                            status['hasDangerousApps'] == true);

      return status;
    } on PlatformException catch (e) {
      print("Failed to get security status: ${e.message}");
      return _getDefaultSecurityStatus();
    }
}

static Map<String, dynamic> _getDefaultSecurityStatus() {
    return {
      'isDevModeEnabled': false, // Assume unsafe by default
      'isRooted': false,
      'isMagiskDetected': false,
      'hasDangerousApps': false,
      'isSecure': false,
      'deviceInfo': {
        'manufacturer': 'unknown',
        'brand': 'unknown',
        'model': 'unknown',
        'device': 'unknown',
        'product': 'unknown',
        'version': 'unknown',
        'sdkInt': 'unknown',
        'fingerprint': 'unknown'
      }
    };
}
}