package com.flutter.libsec.securesafelibs

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "safesecurelibs"
    private lateinit var safeSecureLib: SafeSecureLib // Declare an instance of SafeSecureLib

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        safeSecureLib = SafeSecureLib() // Initialize the SafeSecureLib instance

        // Register the method channel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "checkSecurityStatus" -> result.success(safeSecureLib.checkSecurityStatus())
                "isDevModeEnabled" -> result.success(safeSecureLib.isDevModeEnabled())
                "isDeviceRooted" -> result.success(safeSecureLib.isDeviceRooted())
                "checkRootMagisk" -> result.success(safeSecureLib.checkRootMagisk())
                "checkDangerousApps" -> result.success(safeSecureLib.checkDangerousApps())
                "getDeviceInfo" -> result.success(safeSecureLib.getDeviceInfo()) // Added method to get device info
                else -> result.notImplemented()
            }
        }
    }
}
