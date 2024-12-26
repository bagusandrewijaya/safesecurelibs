import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'safesecurelibs_method_channel.dart';

abstract class SafesecurelibsPlatform extends PlatformInterface {
  /// Constructs a SafesecurelibsPlatform.
  SafesecurelibsPlatform() : super(token: _token);

  static final Object _token = Object();

  static SafesecurelibsPlatform _instance = MethodChannelSafesecurelibs();

  /// The default instance of [SafesecurelibsPlatform] to use.
  ///
  /// Defaults to [MethodChannelSafesecurelibs].
  static SafesecurelibsPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [SafesecurelibsPlatform] when
  /// they register themselves.
  static set instance(SafesecurelibsPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
