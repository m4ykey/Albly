# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.m4ykey.auth.di.AuthNetworkModuleKt { *; }
-keep class com.m4ykey.auth.di.DataStoreModuleKt { *; }
-keep class com.m4ykey.auth.di.ScopeModuleKt { *; }
-keep class com.m4ykey.auth.di.SpotifyApiModuleKt { *; }
-keep class com.m4ykey.lyrics.presentation.LyricsScreenKt { *; }

-keepclassmembers class * {
    @org.koin.core.annotation.KoinInternalApi *;
}

-keepnames class com.m4ykey.** { *; }