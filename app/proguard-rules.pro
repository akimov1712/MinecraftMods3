# =============================================================================
# CleverAdsSolutions (CAS) mediation + adapter SDKs
# =============================================================================
# Most adapters resolve their entry classes via Class.forName at runtime, so
# everything below has to survive R8 by name.

-keep class com.cleveradssolutions.** { *; }
-keep class com.cleversolutions.** { *; }
-dontwarn com.cleveradssolutions.**
-dontwarn com.cleversolutions.**

# Google Mobile Ads (Google adapter)
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.common.** { *; }
-dontwarn com.google.android.gms.**

# Meta Audience Network
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

# AppLovin
-keep class com.applovin.** { *; }
-dontwarn com.applovin.**

# Unity Ads
-keep class com.unity3d.ads.** { *; }
-keep class com.unity3d.services.** { *; }
-dontwarn com.unity3d.**

# ironSource
-keep class com.ironsource.** { *; }
-keep class com.ironsource.adqualitysdk.** { *; }
-dontwarn com.ironsource.**

# Mintegral
-keep class com.mbridge.** { *; }
-dontwarn com.mbridge.**

# Pangle
-keep class com.bytedance.sdk.** { *; }
-keep class com.pangle.** { *; }
-dontwarn com.bytedance.sdk.**
-dontwarn com.pangle.**

# Chartboost
-keep class com.chartboost.** { *; }
-dontwarn com.chartboost.**

# InMobi
-keep class com.inmobi.** { *; }
-dontwarn com.inmobi.**

# Liftoff / Vungle
-keep class com.vungle.** { *; }
-dontwarn com.vungle.**

# Bigo
-keep class sg.bigo.ads.** { *; }
-dontwarn sg.bigo.ads.**

# Yango / Yandex
-keep class com.yandex.mobile.ads.** { *; }
-dontwarn com.yandex.mobile.ads.**

# Smaato
-keep class com.smaato.sdk.** { *; }
-dontwarn com.smaato.sdk.**

# Ogury
-keep class co.ogury.** { *; }
-dontwarn co.ogury.**

# YSO
-keep class com.ysocorp.** { *; }
-dontwarn com.ysocorp.**

# Maticoo
-keep class com.maticoo.sdk.** { *; }
-dontwarn com.maticoo.sdk.**

# HyprMX
-keep class com.hyprmx.** { *; }
-dontwarn com.hyprmx.**

# StartIO
-keep class com.startapp.** { *; }
-dontwarn com.startapp.**

# Kidoz
-keep class com.kidoz.sdk.** { *; }
-dontwarn com.kidoz.sdk.**

# SuperAwesome
-keep class tv.superawesome.** { *; }
-dontwarn tv.superawesome.**

# Prado
-keep class com.prado.** { *; }
-dontwarn com.prado.**

# DT Exchange (Fyber)
-keep class com.fyber.** { *; }
-dontwarn com.fyber.**

# =============================================================================
# AppMetrica
# =============================================================================
-keep class io.appmetrica.** { *; }
-dontwarn io.appmetrica.**

# =============================================================================
# Hilt / Dagger — generally handled by the plugin, but keep injector classes
# =============================================================================
-keep class dagger.hilt.** { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }

# =============================================================================
# Kotlin reflection / coroutines metadata
# =============================================================================
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
-keepattributes Signature,InnerClasses,EnclosingMethod

# Coroutines internals
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
