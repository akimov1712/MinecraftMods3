package dev.akmvxx.ads.nativead

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.graphics.toArgb
import com.cleveradssolutions.sdk.nativead.CASChoicesView
import com.cleveradssolutions.sdk.nativead.CASMediaView
import com.cleveradssolutions.sdk.nativead.CASNativeView
import dev.akmvxx.ui.AppColors
import dev.akmvxx.ui.R as UiR

internal data class NativeAdViews(
    val root: CASNativeView,
    val title: TextView,
    val body: TextView,
    val advertiser: TextView,
    val callToAction: Button,
    val media: CASMediaView,
    val icon: ImageView,
    val adChoices: CASChoicesView,
    val adLabel: TextView,
)

internal fun buildInlineNativeAdView(context: Context): NativeAdViews {
    val root = CASNativeView(context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        setPadding(10.dp(), 10.dp(), 10.dp(), 10.dp())
        minimumHeight = 300.dp()
    }

    val container = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = LinearLayout.VERTICAL
    }

    val header = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    val adLabel = TextView(context).apply {
        text = context.getString(UiR.string.ad)
        setTextColor(AppColors.TextWhite.toArgb())
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setTypeface(typeface, Typeface.BOLD)
        setPadding(6.dp(), 2.dp(), 6.dp(), 2.dp())
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            weight = 1f
        }
    }

    val adChoices = CASChoicesView(context).apply {
        layoutParams = LinearLayout.LayoutParams(32.dp(), 32.dp())
    }

    header.addView(adLabel)
    header.addView(adChoices)

    val mediaWrapper = FrameLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 6.dp()
        }
    }

    val media = CASMediaView(context).apply {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        minimumHeight = 120.dp()
    }

    val icon = ImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(48.dp(), 48.dp()).apply {
            gravity = Gravity.TOP or Gravity.START
            setMargins(4.dp(), 4.dp(), 4.dp(), 4.dp())
        }
    }

    mediaWrapper.addView(media)
    mediaWrapper.addView(icon)

    val title = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 10.dp()
        }
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 21f)
        setTextColor(AppColors.TextWhite.toArgb())
        setTypeface(typeface, Typeface.BOLD)
    }

    val advertiser = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        setTextColor(AppColors.Shimmer.toArgb())
    }

    val body = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 6.dp()
        }
        maxLines = 4
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        setTextColor(AppColors.Shimmer.toArgb())
    }

    val cta = Button(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 48.dp()).apply {
            topMargin = 10.dp()
        }
        isAllCaps = false
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        setTextColor(AppColors.Black.toArgb())
        setTypeface(typeface, Typeface.BOLD)
        background = roundedDrawable(AppColors.White.toArgb(), 12.dp().toFloat())
        backgroundTintList = ColorStateList.valueOf(AppColors.White.toArgb())
    }

    container.addView(header)
    container.addView(mediaWrapper)
    container.addView(title)
    container.addView(advertiser)
    container.addView(body)
    container.addView(cta)

    root.addView(container)

    return NativeAdViews(
        root = root,
        title = title,
        body = body,
        advertiser = advertiser,
        callToAction = cta,
        media = media,
        icon = icon,
        adChoices = adChoices,
        adLabel = adLabel,
    )
}

internal fun buildFullscreenNativeAdView(context: Context): NativeAdViews {
    val root = CASNativeView(context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        setBackgroundColor(AppColors.BackgroundPrimary.toArgb())
    }

    val container = LinearLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        orientation = LinearLayout.VERTICAL
        setPadding(12.dp(), 24.dp(), 12.dp(), 32.dp())
    }

    val header = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    val icon = ImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(48.dp(), 48.dp()).apply {
            weight = 0f
        }
    }

    val spacer = View(context).apply {
        layoutParams = LinearLayout.LayoutParams(0, 0).apply { weight = 1f }
    }

    val adChoices = CASChoicesView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    header.addView(icon)
    header.addView(spacer)
    header.addView(adChoices)

    val media = CASMediaView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 0).apply {
            weight = 1f
            topMargin = 16.dp()
            bottomMargin = 16.dp()
        }
    }

    val adLabel = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        text = context.getString(UiR.string.ad)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setTextColor(Color.WHITE)
        setPadding(6.dp(), 2.dp(), 6.dp(), 2.dp())
    }

    val titleRow = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 8.dp()
        }
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    val title = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT).apply { weight = 1f }
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        setTextColor(AppColors.TextWhite.toArgb())
        setTypeface(typeface, Typeface.BOLD)
    }

    val advertiser = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            marginStart = 8.dp()
        }
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTextColor(AppColors.Shimmer.toArgb())
    }

    titleRow.addView(title)
    titleRow.addView(advertiser)

    val body = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 8.dp()
            bottomMargin = 20.dp()
        }
        maxLines = 4
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setTextColor(AppColors.Shimmer.toArgb())
    }

    val cta = Button(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 48.dp())
        isAllCaps = false
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setTextColor(AppColors.Black.toArgb())
        setTypeface(typeface, Typeface.BOLD)
        background = roundedDrawable(AppColors.White.toArgb(), 12.dp().toFloat())
        backgroundTintList = ColorStateList.valueOf(AppColors.White.toArgb())
    }

    container.addView(header)
    container.addView(media)
    container.addView(adLabel)
    container.addView(titleRow)
    container.addView(body)
    container.addView(cta)

    root.addView(container)

    return NativeAdViews(
        root = root,
        title = title,
        body = body,
        advertiser = advertiser,
        callToAction = cta,
        media = media,
        icon = icon,
        adChoices = adChoices,
        adLabel = adLabel,
    )
}

internal fun NativeAdViews.bindToRoot() {
    root.headlineView = title
    root.bodyView = body
    root.advertiserView = advertiser
    root.callToActionView = callToAction
    root.mediaView = media
    root.iconView = icon
    root.adChoicesView = adChoices
    root.adLabelView = adLabel
}

private fun roundedDrawable(color: Int, radiusPx: Float): GradientDrawable =
    GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        cornerRadius = radiusPx
        setColor(color)
    }

private fun Int.dp(): Int =
    (this * android.content.res.Resources.getSystem().displayMetrics.density).toInt()

private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
