package dev.akmvxx.app

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AppReview"
private const val PREFS_NAME = "app_review"
private const val KEY_APP_OPEN_COUNT = "app_open_count"
private const val KEY_REVIEW_SHOWN = "review_shown"

/** App opens at which the in-app review prompt is requested. */
private val TRIGGER_OPENS = setOf(3, 7, 15)

/**
 * Drives the Google Play in-app review flow.
 *
 * Counts cold app opens in SharedPreferences. When the count hits one of
 * [TRIGGER_OPENS] thresholds and the user has not already been prompted in
 * this lifetime, asks the Play SDK to launch the review form.
 *
 * Google Play caps the actual frequency on its side (no more than a handful
 * of times per year regardless of how often we call), so the local counter
 * is just to avoid burning a request on the very first launch.
 */
@Singleton
class AppReview @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val manager: ReviewManager by lazy {
        if (BuildConfig.DEBUG) FakeReviewManager(context)
        else ReviewManagerFactory.create(context)
    }

    fun maybeRequest(activity: Activity) {
        if (prefs.getBoolean(KEY_REVIEW_SHOWN, false)) return

        val opens = prefs.getInt(KEY_APP_OPEN_COUNT, 0) + 1
        prefs.edit { putInt(KEY_APP_OPEN_COUNT, opens) }

        if (opens !in TRIGGER_OPENS) return

        manager.requestReviewFlow()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "requestReviewFlow failed: ${task.exception}")
                    return@addOnCompleteListener
                }
                manager.launchReviewFlow(activity, task.result)
                    .addOnCompleteListener {
                        prefs.edit { putBoolean(KEY_REVIEW_SHOWN, true) }
                    }
            }
    }
}
