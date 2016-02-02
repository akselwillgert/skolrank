# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\eakswil\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault, InnerClasses
#-keep class com.octo.** { *;}

#RoboSpice requests should be preserved in most cases
-keepclassmembers class se.subsurface.skolrank.model.** {
  public void set*(***);
  public *** get*();
  public *** is*();
  public <init>(***);
}

-keepclassmembers class se.subsurface.skolrank.json.** {
  public void set*(***);
  public *** get*();
  public *** is*();
  public <init>(***);
}

-keep public class se.subsurface.** {
  public protected private *;
  *;
}
-keepattributes SourceFile,LineNumberTable

#-keep class org.codehaus.** { *; }

#Warnings to be removed. Otherwise maven plugin stops, but not dangerous
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**
-dontwarn com.octo.android.robospice.persistence.**
-dontwarn sun.misc.Unsafe
-dontwarn org.springframework.**
-dontwarn org.codehaus.**
-dontwarn com.sothree.**
-dontwarn de.psdev.**
-dontwarn com.google.**
-dontwarn com.octo.android.**
