import 'package:flutter/services.dart';
import 'dart:io' show Platform;

class CheckerMethode {
  static const platform = MethodChannel('safesecurelibs');

  static Future<Map<String, bool>> checkSecurityStatus() async {
    if (!Platform.isAndroid) {
      return {
        'isDevModeEnabled': false,
        'isRooted': false,
        'isMagiskDetected': false,
        'hasDangerousApps': false
      };
    }

    try {
      final Map<dynamic, dynamic> result = await platform.invokeMethod('checkSecurityStatus');
      return Map<String, bool>.from(result);
    } on PlatformException catch (e) {
      print("Failed to get security status: ${e.message}");
      return {
        'isDevModeEnabled': false,
        'isRooted': false,
        'isMagiskDetected': false,
        'hasDangerousApps': false
      };
    }
  }

  static Future<bool> isDevModeEnabled() async {
    if (!Platform.isAndroid) {
      return false;
    }

    try {
      final bool result = await platform.invokeMethod('isDevModeEnabled');
      return result;
    } on PlatformException catch (e) {
      print("Failed to check dev mode: ${e.message}");
      return false;
    }
  }

  static Future<bool> isDeviceRooted() async {
    if (!Platform.isAndroid) {
      return false;
    }
    
    try {
      final bool result = await platform.invokeMethod('isDeviceRooted');
      return result;
    } on PlatformException catch (e) {
      print("Failed to check root status: ${e.message}");
      return false;
    }
  }
}
