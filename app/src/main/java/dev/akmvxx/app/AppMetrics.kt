package dev.akmvxx.app

import android.app.Application
import android.util.Log
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

private const val TAG = "AppMetrics"

fun Application.setupMetrics() {
    val key = BuildConfig.METRICA_API_KEY
    if (key.isBlank()) {
        Log.w(TAG, "METRICA_API_KEY is blank — analytics skipped")
        return
    }

    val config = AppMetricaConfig.newConfigBuilder(key)
        .withSessionTimeout(60)
        .withLogs()
        .build()
    AppMetrica.activate(this, config)
    AppMetrica.enableActivityAutoTracking(this)
}

object AppEvents {

    fun report(name: String, attrs: Map<String, Any?> = emptyMap()) {
        if (BuildConfig.METRICA_API_KEY.isBlank()) return
        if (attrs.isEmpty()) {
            AppMetrica.reportEvent(name)
        } else {
            AppMetrica.reportEvent(name, attrs)
        }
    }
}
