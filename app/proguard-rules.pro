# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the Android SDK proguard-android-optimize.txt file.

# Keep Hilt-generated classes
-keep class dagger.hilt.** { *; }

# Keep Room entities
-keep class com.focus.app.data.database.entity.** { *; }
