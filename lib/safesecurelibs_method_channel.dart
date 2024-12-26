import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'safesecurelibs_platform_interface.dart';

/// An implementation of [SafesecurelibsPlatform] that uses method channels.
class MethodChannelSafesecurelibs extends SafesecurelibsPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('safesecurelibs');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
