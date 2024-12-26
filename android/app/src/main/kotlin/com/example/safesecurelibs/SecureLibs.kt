package com.example.safesecurelibs

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import java.io.File
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class SafeSecureLib(private val context: Context) : FlutterPlugin {
    companion object {
        private const val TAG = "SafeSecureLib"
        private const val CHANNEL = "safesecurelibs"
    }

    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, CHANNEL)
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "checkSecurityStatus" -> result.success(checkSecurityStatus())
                "isDevModeEnabled" -> result.success(isDevModeEnabled())
                "isDeviceRooted" -> result.success(isDeviceRooted())
                // Add other method calls as needed
                else -> result.notImplemented()
            }
        }
    }

    fun checkSecurityStatus(): Map<String, Boolean> {
        return try {
            mapOf(
                "isDevModeEnabled" to isDevModeEnabled(),
                "isRooted" to isDeviceRooted(),
                "isMagiskDetected" to checkRootMagisk(),
                "hasDangerousApps" to checkDangerousApps()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting security status", e)
            mapOf(
                "isDevModeEnabled" to false,
                "isRooted" to false,
                "isMagiskDetected" to false,
                "hasDangerousApps" to false
            )
        }
    }

    fun isDevModeEnabled(): Boolean {
        return try {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                0
            ) != 0
        } catch (e: Exception) {
            Log.e(TAG, "Error checking dev mode", e)
            false
        }
    }

    fun isDeviceRooted(): Boolean {
        return try {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
            )
            paths.any { File(it).exists() } || checkRootMagisk()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking root status", e)
            false
        }
    }

    private fun checkRootMagisk(): Boolean {
        return try {
            val magiskPaths = arrayOf(
                "/sbin/magisk",
                "/system/xbin/magisk",
                "/system/bin/magisk",
                "/data/adb/magisk",
                "/data/data/com.topjohnwu.magisk"
            )
            magiskPaths.any { File(it).exists() }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking Magisk", e)
            false
        }
    }

    private fun checkDangerousApps(): Boolean {
        val dangerousPackages = arrayOf(
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

        return try {
            val packageManager = context.packageManager
            dangerousPackages.any { packageName ->
                try {
                    packageManager.getPackageInfo(packageName, 0)
                    true
                } catch (e: PackageManager.NameNotFoundException) {
                    false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking dangerous apps", e)
            false
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        // Clean up if necessary
    }
}