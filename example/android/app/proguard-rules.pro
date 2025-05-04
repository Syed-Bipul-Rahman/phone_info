# Keep classes used by the phone_info plugin
-keep class me.bipul.phone_info.phone_info_plugin.** { *; }

# Keep Android classes used by the plugin
-keep class android.hardware.** { *; }
-keep class android.os.** { *; }
-keep class androidx.biometric.** { *; }
-keep class androidx.camera.** { *; }

# Prevent obfuscation of classes used in reflection
-keep class androidx.core.** { *; }