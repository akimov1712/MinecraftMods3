# =============================================================================
# Yodo1 MAS SDK + mediation adapters
# Mirrors the rules from
# https://developers.yodo1.com/docs/sdk/advanced/proguard
# =============================================================================
-ignorewarnings
-keeppackagenames com.yodo1.**
-keep class com.yodo1.** { *; }
-keep class com.yodo1.mas.** { *; }
-keep class com.yodo1.mas.ad.** {*;}
-keep class com.yodo1.mas.ads.** {*;}
-keep class com.yodo1.mas.error.** { *; }
-keep class com.yodo1.mas.event.** { *; }
-keep public class * extends com.yodo1.mas.mediation.Yodo1MasAdapterBase
-keep public class * extends com.yodo1.mas.ad.Yodo1MasAdAdapterBase

# Google Mobile Ads / Play Services
-keep class com.google.ads.** { *; }
-keep public class com.google.android.gms.ads.** {public *;}
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.appset.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{public *;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{public *;}
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.ads.identifier.**
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {public static final *** NULL;}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {@com.google.android.gms.common.annotation.KeepName *;}

# ironSource
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {public *;}
-keep class com.ironsource.adapters.** {*;}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepclassmembers class com.ironsource.** { public *; }
-keep public class com.ironsource.**
-keep public interface com.ironsource.mediationsdk.sdk** {*; }
-keep public interface com.ironsource.mediationsdk.impressionData.ImpressionDataListener {*; }
-dontwarn com.ironsource.adapters.unityads.**

# Unity Ads
-keep class com.unity3d.ads.** {*;}
-keep class com.unity3d.services.** {*;}
-dontwarn com.unity3d.services.**
-dontwarn com.google.ar.core.**

# AppLovin
-keep public class com.applovin.sdk.AppLovinSdk{*;}
-keep public class com.applovin.sdk.AppLovin* {public protected *;}
-keep public class com.applovin.nativeAds.AppLovin* {public protected *;}
-keep public class com.applovin.adview.* {public protected *;}
-keep public class com.applovin.mediation.* {public protected *;}
-keep public class com.applovin.mediation.ads.* {public protected *;}
-keep public class com.applovin.impl.*.AppLovin {public protected *;}
-keep public class com.applovin.impl.**.*Impl {public protected *;}
-keepclassmembers class com.applovin.sdk.AppLovinSdkSettings {private java.util.Map localSettings;}
-keep class com.applovin.mediation.adapters.** {*;}
-keep class com.applovin.mediation.adapter.**{*;}

# Chartboost
-keep class com.chartboost.** {*;}

# Meta (Facebook Audience Network)
-dontwarn com.facebook.ads.internal.**
-keeppackagenames com.facebook.*
-keep public class com.facebook.ads.** {public protected *;}
-keepclassmembers class com.facebook.ads.internal.AdSdkVersion { static *; }
-keepclassmembers class com.facebook.ads.internal.settings.AdSdkVersion { static *; }
-keepclassmembers class com.facebook.ads.BuildConfig { static *; }
-keep public interface com.facebook.ads** {*; }

# Tapjoy
-keep class com.tapjoy.** { *; }
-dontwarn com.tapjoy.**

# Vungle / Liftoff Monetize
-dontwarn com.vungle.ads.**
-keepclassmembers class com.vungle.ads.** { *; }
-keep public interface com.vungle.warren.PlayAdCallback {*; }
-keep public interface com.vungle.warren.ui.contract** {*; }
-keep public interface com.vungle.warren.ui.view** {*; }

# Moat / IAB SDK (viewability)
-dontwarn com.moat.**
-keep class com.moat.** { public protected private *; }
-keep class com.iab.** {*;}
-dontwarn com.iab.**

# Yandex
-keep class com.yandex.mobile.ads.** {*;}
-dontwarn com.yandex.mobile.ads.**

# myTarget
-keep class com.my.target.** {*;}

# Pangle (ByteDance)
-keep class com.bytedance.sdk.** { *; }

# Tencent Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# Sensors Analytics (CN networks)
-dontwarn com.sensorsdata.analytics.android.**
-keep class com.sensorsdata.analytics.android.** {*;}
-keep class com.yodo1.sensor.** {*;}

# InMobi
-keep class com.inmobi.** {*;}
-keep public interface com.inmobi.ads.listeners** {*; }
-keep public interface com.inmobi.ads.InMobiInterstitial** {*; }
-keep public interface com.inmobi.ads.InMobiBanner** {*; }

# DT Exchange / Fyber
-keep public interface com.fyber.inneractive.sdk.external** {*; }
-keep public interface com.fyber.inneractive.sdk.activities** {*; }
-keep public interface com.fyber.inneractive.sdk.ui** {*; }

# Mintegral / Mbridge
-keep public interface com.mbridge.msdk.out** {*; }
-keep public interface com.mbridge.msdk.videocommon.listener** {*; }
-keep public interface com.mbridge.msdk.interstitialvideo.out** {*; }
-keep public interface com.mintegral.msdk.out** {*; }
-keep public interface com.mintegral.msdk.videocommon.listener** {*; }
-keep public interface com.mintegral.msdk.interstitialvideo.out** {*; }

# YSO Network
-keep class com.ysocorp.ysonetwork.* { *; }

# Maticoo
-keep class com.maticoo.sdk.**{*;}
-keep class com.maticooad.sdk.**{*;}

# TradPlus / TopOn / Anythink
-keep public class com.tradplus.** { *; }
-keep class com.tradplus.ads.** { *; }

# OEM helper (UC / Heytap / Zui / Miui / Vivo)
-keep class com.umeng.** {*;}
-keep class com.uc.** {*;}
-keep class com.uc.crashsdk.** { *; }
-keep interface com.uc.crashsdk.** { *; }
-keep class com.zui.** {*;}
-keep class com.miui.** {*;}
-keep class com.heytap.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}

# Misc thirdparty
-keep class io.ktor.**
-keep class cn.thinkinganalyticsclone.android.** { *; }
-dontwarn cn.thinkinganalyticsclone.android.thirdparty.**

# Resources / Bundles
-keep class **.R$* {<fields>;}
-keepclassmembers class **.R$* { public static <fields>; }

# Android framework
-keep public class * extends android.content.ContentProvider
-keepnames class * extends android.view.View
-keep class * extends android.app.Activity
-keep class * extends android.app.Fragment {public void setUserVisibleHint(boolean);public void onHiddenChanged(boolean);public void onResume();public void onPause();}
-keep class android.support.v4.app.Fragment {public void setUserVisibleHint(boolean);public void onHiddenChanged(boolean);public void onResume();public void onPause();}
-keep class * extends android.support.v4.app.Fragment {public void setUserVisibleHint(boolean);public void onHiddenChanged(boolean);public void onResume();public void onPause();}
-keepclassmembers class * implements android.os.Parcelable {public static final android.os.Parcelable$Creator *;}
-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }

# AndroidX
-keep class androidx.localbroadcastmanager.content.LocalBroadcastManager { *; }
-keep class androidx.recyclerview.widget.RecyclerView { *; }
-keep class androidx.recyclerview.widget.RecyclerView$OnScrollListener { *; }

# WebView interfaces (required by IronSource / DT Exchange)
-keepattributes JavascriptInterface
-keep class android.webkit.JavascriptInterface {*;}
-keepclassmembers class * { @android.webkit.JavascriptInterface <methods>; }

# Gson + protobuf
-keep class com.google.gson.** { *; }
-dontwarn com.google.protobuf.**
-keepclassmembers class com.google.protobuf.** { *; }
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# OkHttp / Retrofit
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
-dontwarn sun.misc.**
-dontwarn org.json.**
-keep class org.json.**{*;}

# Picasso (legacy mediation)
-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** {*;}

# Common attributes
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature,InnerClasses,Exceptions,Annotation
-keepattributes *Annotation*,InnerClasses

# Resource bundles, enums and JSONObject constructors
-keepclassmembers class * {public <init> (org.json.JSONObject);}
-keepclassmembers enum * {public static **[] values();public static ** valueOf(java.lang.String);}
-keep class * extends java.util.ListResourceBundle {protected Object[][] getContents();}

-dontskipnonpubliclibraryclasses

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
