import Cocoa
import FlutterMacOS

@main
class AppDelegate: FlutterAppDelegate {
  private let CHANNEL = "safesecurelibs"

  override func applicationShouldTerminateAfterLastWindowClosed(_ sender: NSApplication) -> Bool {
    return true
  }

  func checkSecurityStatus() -> (isDevModeEnabled: Bool, isRooted: Bool, isMagiskDetected: Bool, hasDangerousApps: Bool) {
    let isJailbroken = checkJailbreak()
    let hasDangerousApps = checkDangerousApps()
    
    return (isDevModeEnabled: false, isRooted: isJailbroken, isMagiskDetected: false, hasDangerousApps: hasDangerousApps)
  }

  private func checkJailbreak() -> Bool {
    let fileManager = FileManager.default
    let jailbreakPaths = [
      "/Applications/Cydia.app",
      "/Library/MobileSubstrate/MobileSubstrate.dylib",
      "/bin/bash",
      "/usr/sbin/sshd",
      "/etc/apt"
    ]
    
    for path in jailbreakPaths {
      if fileManager.fileExists(atPath: path) {
        return true
      }
    }
    
    return false
  }

  private func checkDangerousApps() -> Bool {
    let fileManager = FileManager.default
    let dangerousApps = [
      "com.topjohnwu.magisk",
      "com.thirdparty.superuser",
      "eu.chainfire.supersu",
      "com.koushikdutta.superuser",
      "com.zachspong.temprootremovejb",
      "com.amphoras.hidemyroot",
      "com.chelpus.lackypatch",
      "com.kingroot.kinguser",
      "com.jrummy.root.browserfree"
    ]
    
    for app in dangerousApps {
      let appPath = "/Applications/\(app).app"
      if fileManager.fileExists(atPath: appPath) {
        return true
      }
    }
    
    return false
  }
  
  // Method channel to communicate with Flutter
  func setupMethodChannel() {
    let controller = FlutterViewController()
    let methodChannel = FlutterMethodChannel(name: CHANNEL, binaryMessenger: controller.binaryMessenger)
    
    methodChannel.setMethodCallHandler { [weak self] call, result in
      if call.method == "checkSecurityStatus" {
        let securityStatus = self?.checkSecurityStatus() ?? (false, false, false, false)
        result([
          "isDevModeEnabled": securityStatus.isDevModeEnabled,
          "isRooted": securityStatus.isRooted,
          "isMagiskDetected": securityStatus.isMagiskDetected,
          "hasDangerousApps": securityStatus.hasDangerousApps
        ])
      } else {
        result(FlutterMethodNotImplemented)
      }
    }
  }
}
