package com.flutter.libsec.securesafelibs.safesecurelibs

import androidx.annotation.NonNull


import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader


/** SafesecurelibsPlugin */
class SafesecurelibsPlugin: FlutterPlugin, MethodCallHandler {
private var channel: MethodChannel? = null
    private var context: Context? = null
    
    companion object {
        private const val TAG = "SafeSecureLib"
        private const val CHANNEL = "safesecurelibs"
        
        // Root detection paths
        private val ROOT_PATHS = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        
        // Root detection paths including Magisk
        private val  MAGISK_PATHS = arrayOf(
            "/sbin/magisk",
            "/system/xbin/magisk",
            "/system/bin/magisk",
            "/data/adb/magisk",
            "/data/data/com.topjohnwu.magisk",
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        // Dangerous packages list
        private val DANGEROUS_PACKAGES = arrayOf(
            "com.topjohnwu.magisk",
            "com.thirdparty.superuser", 
            "eu.chainfire.supersu",
            "com.noshufou.android.su",
            "com.koushikdutta.superuser",
            "com.zachspong.temprootremovejb",
            "com.ramdroid.appquarantine",
            "com.formyhm.hideroot",
            "com.amphoras.hidemyroot",
            "com.saurik.substrate",
            "de.robv.android.xposed",
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "com.android.vending.billing.InAppBillingService.COIN",
            "com.chelpus.lackypatch",
            "com.kingroot.kinguser",
            "com.kingo.root",
            "com.smedialink.oneclickroot",
            "com.zhiqupk.root.global",
            "com.alephzain.framaroot",
            "com.koushikdutta.rommanager",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.luckypatcher",
            "com.yellowes.su",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.bitcnew.blockchain",
            "com.noshufou.android.su.elite",
            "com.jrummy.root.browserfree",
            "com.jrummy.busybox.installer"
        )
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL)
        channel?.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (context == null) {
            result.error("NO_CONTEXT", "Plugin context not initialized", null)
            return
        }

        try {
            when (call.method) {
                "checkSecurityStatus" -> result.success(checkSecurityStatus())
                "isDevModeEnabled" -> result.success(isDevModeEnabled())
                "isDeviceRooted" -> result.success(isDeviceRooted())
                "checkRootMagisk" -> result.success(checkRootMagisk())
                "checkDangerousApps" -> result.success(checkDangerousApps())
                "getDeviceInfo" -> result.success(getDeviceInfo())
                else -> result.notImplemented()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in method call: ${call.method}", e)
            result.error("ERROR", e.message, e.stackTrace.toString())
        }
    }

    fun checkSecurityStatus(): Map<String, Any> { // Changed to public
        return try {
            val devMode = isDevModeEnabled()
            val rooted = isDeviceRooted()
            val magisk = checkRootMagisk()
            val dangerousApps = checkDangerousApps()
            val deviceInfo = getDeviceInfo()
            
            mapOf(
                "isDevModeEnabled" to devMode,
                "isRooted" to rooted,
                "isMagiskDetected" to magisk,
                "hasDangerousApps" to dangerousApps,
                "isSecure" to !(devMode || rooted || magisk || dangerousApps),
                "deviceInfo" to deviceInfo
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting security status", e)
            mapOf(
                "isDevModeEnabled" to false,
                "isRooted" to false,
                "isMagiskDetected" to false,
                "hasDangerousApps" to false,
                "isSecure" to true,
                "deviceInfo" to getDeviceInfo()
            )
        }
    }

    fun isDevModeEnabled(): Boolean {
    return try {
        context?.let { ctx ->
            var devOptionsEnabled = false
            
            try {
                // Check development settings enabled
                devOptionsEnabled = Settings.Secure.getInt(ctx.contentResolver, 
                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
            } catch (e: Settings.SettingNotFoundException) {
                try {
                    // Fallback to Global settings if Secure fails
                    devOptionsEnabled = Settings.Global.getInt(ctx.contentResolver,
                        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
                } catch (e: Exception) {
                    Log.e(TAG, "Error checking dev settings in Global", e)
                }
            }

            // Check ADB
            var adbEnabled = false
            try {
                adbEnabled = Settings.Global.getInt(ctx.contentResolver,
                    Settings.Global.ADB_ENABLED, 0) == 1
            } catch (e: Exception) {
                Log.e(TAG, "Error checking ADB", e)
            }

            // Check USB Debugging
            var usbDebugging = false
            try {
                usbDebugging = Settings.Secure.getInt(ctx.contentResolver,
                    Settings.Secure.ADB_ENABLED, 0) == 1
            } catch (e: Exception) {
                Log.e(TAG, "Error checking USB debugging", e)
            }

            // Check if running on emulator
            val isEmulator = Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) ||
                "google_sdk" == Build.PRODUCT

            // Log all checks
            Log.d(TAG, """
                Dev Options Check Results:
                Development Settings: $devOptionsEnabled
                ADB Enabled: $adbEnabled
                USB Debugging: $usbDebugging
                Is Emulator: $isEmulator
            """.trimIndent())

            // If running on emulator, consider it as dev mode
            val result = devOptionsEnabled || adbEnabled || usbDebugging || isEmulator
            Log.d(TAG, "Final Dev Mode Result: $result")
            
            result
        } ?: run {
            Log.e(TAG, "Context is null")
            true // Assume dev mode if context is null for safety
        }
    } catch (e: Exception) {
        Log.e(TAG, "Error in isDevModeEnabled", e)
        true // Assume dev mode if there's an error
    }
}

    fun isDeviceRooted(): Boolean { // Changed to public
        return try {
            // Check for root binary
            val hasRootBinary = ROOT_PATHS.any { File(it).exists() }
            
            // Check for root permissions
            val hasRootPermissions = try {
                val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val output = reader.readText()
                process.waitFor()
                output.contains("uid=0")
            } catch (e: Exception) {
                false
            }

            // Check build tags
            val hasDangerousBuildTags = Build.TAGS?.contains("test-keys") ?: false
            
            hasRootBinary || hasRootPermissions || hasDangerousBuildTags || checkRootMagisk()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking root status", e)
            false
        }
    }

    fun checkRootMagisk(): Boolean { // Changed to public
        return try {
            context?.let { ctx ->
                // Check Magisk paths
                val hasMagiskPaths = MAGISK_PATHS.any { File(it).exists() }
                
                // Check Magisk package
                val hasMagiskPackage = try {
                    ctx.packageManager.getPackageInfo("com.topjohnwu.magisk", 0)
                    true
                } catch (e: PackageManager.NameNotFoundException) {
                    false
                }
                
                hasMagiskPaths || hasMagiskPackage
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Error checking Magisk", e)
            false
        }
    }

    fun checkDangerousApps(): Boolean { // Changed to public
        return try {
            context?.let { ctx ->
                val packageManager = ctx.packageManager
                DANGEROUS_PACKAGES.any { packageName ->
                    try {
                        packageManager.getPackageInfo(packageName, 0)
                        true
                    } catch (e: PackageManager.NameNotFoundException) {
                        false
                    }
                }
            } ?: false
        } catch (e: Exception) {
            Log.e(TAG, "Error checking dangerous apps", e)
            false
        }
    }

    // Removed the override keyword since this method does not override any method from the superclass
    fun getDeviceInfo(): Map<String, String> {
        return mapOf(
            "manufacturer" to Build.MANUFACTURER,
            "brand" to Build.BRAND,
            "model" to Build.MODEL,
            "device" to Build.DEVICE,
            "product" to Build.PRODUCT,
            "version" to Build.VERSION.RELEASE,
            "sdkInt" to Build.VERSION.SDK_INT.toString(),
            "fingerprint" to Build.FINGERPRINT
        )
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
        channel = null
        context = null
    }
}
