import 'package:flutter_test/flutter_test.dart';
import 'package:safesecurelibs/safesecurelibs.dart';
import 'package:safesecurelibs/safesecurelibs_platform_interface.dart';
import 'package:safesecurelibs/safesecurelibs_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockSafesecurelibsPlatform
    with MockPlatformInterfaceMixin
    implements SafesecurelibsPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final SafesecurelibsPlatform initialPlatform = SafesecurelibsPlatform.instance;

  test('$MethodChannelSafesecurelibs is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelSafesecurelibs>());
  });

  test('getPlatformVersion', () async {
    Safesecurelibs safesecurelibsPlugin = Safesecurelibs();
    MockSafesecurelibsPlatform fakePlatform = MockSafesecurelibsPlatform();
    SafesecurelibsPlatform.instance = fakePlatform;

    expect(await safesecurelibsPlugin.getPlatformVersion(), '42');
  });
}
