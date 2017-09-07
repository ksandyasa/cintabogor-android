# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/apridosandyasa/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class android.support.v7.** { *; }
#-dontwarn android.support.v7.**
-dontwarn com.squareup.okhttp3.**
-dontwarn com.squareup.picasso.**
-dontwarn okio.**
-dontwarn org.apache.lang.**
-dontwarn de.hdodenhof.**
-dontwarn com.github.ugurtekbas.**
-keep class om.nhaarman.listviewanimations.** { *; }
#-dontwarn om.nhaarman.listviewanimations.**
-dontwarn com.nineoldandroids.**
-keep class se.emilsjolander.** { *; }
-dontwarn se.emilsjolander.**


