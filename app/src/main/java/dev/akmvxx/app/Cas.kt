package dev.akmvxx.app

import android.content.Context
import com.cleversolutions.ads.android.CAS
import com.facebook.ads.AdSettings

fun Context.setupCas(casId: String) {
    val builder = CAS.buildManager()
        .withCasId(casId)
        .withCompletionListener { }

    AdSettings.setDataProcessingOptions(arrayOf())
    builder.build(this)
}
