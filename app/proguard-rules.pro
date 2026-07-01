# =============================================================================
# Resources / Bundles
# =============================================================================
-keep class **.R$* {<fields>;}
-keepclassmembers class **.R$* { public static <fields>; }

# =============================================================================
# Android framework
# =============================================================================
-keep public class * extends android.content.ContentProvider
-keepnames class * extends android.view.View
-keep class * extends android.app.Activity
-keepclassmembers class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *;}
-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }

# =============================================================================
# Common attributes
# =============================================================================
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature,InnerClasses,Exceptions,Annotation
-keepattributes *Annotation*,InnerClasses
-keepclassmembers enum * {public static **[] values();public static ** valueOf(java.lang.String);}

# =============================================================================
# Gson
# =============================================================================
-keep class com.google.gson.** { *; }
-dontwarn com.google.protobuf.**
-keepclassmembers class com.google.protobuf.** { *; }
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# =============================================================================
# OkHttp / Retrofit
# =============================================================================
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz
-dontwarn okio.**
-dontwarn okhttp3.internal.platform.**
-dontwarn retrofit2.Platform$Java8
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn javax.annotation.**

# =============================================================================
# AppMetrica
# =============================================================================
-keep class io.appmetrica.** { *; }
-dontwarn io.appmetrica.**

# =============================================================================
# Hilt / Kotlin coroutines
# =============================================================================
-keep class dagger.hilt.** { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
