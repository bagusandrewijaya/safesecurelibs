package com.example.safesecurelibs

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "safesecurelibs"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "checkSecurityStatus" -> {
                    val safeSecureLib = SafeSecureLib(context)
                    val securityStatus = safeSecureLib.checkSecurityStatus()
                    result.success(securityStatus)
                }
                "isDevModeEnabled" -> {
                    val safeSecureLib = SafeSecureLib(context)
                    val isDevMode = safeSecureLib.isDevModeEnabled()
                    result.success(isDevMode)
                }
                "isDeviceRooted" -> {
                    val safeSecureLib = SafeSecureLib(context)
                    val isRooted = safeSecureLib.isDeviceRooted()
                    result.success(isRooted)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}
