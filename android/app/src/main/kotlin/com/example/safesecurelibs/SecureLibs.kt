package com.example.safesecurelibs


import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import java.io.File

class SafeSecureLib(private val context: Context) {
    companion object {
        private const val TAG = "SafeSecureLib"
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
            "com.chelpus.lackypatch"
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
}