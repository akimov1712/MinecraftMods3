package dev.akmvxx.ads.nativead

import android.content.Context
import android.content.res.ColorStateList
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

private val AdAccent = AppColors.CategoryAmber
private val CardSurface = AppColors.BackgroundSecondary
private val CardBorder = AppColors.Outlined
private val BodyTextColor = AppColors.TextWhite.copy(alpha = 0.72f)

internal fun buildInlineNativeAdView(context: Context): NativeAdViews {
    val root = CASNativeView(context).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        background = strokedRoundedDrawable(
            fill = CardSurface.toArgb(),
            stroke = AdAccent.toArgb(),
            strokeWidthPx = 1.dp(),
            radiusPx = 8.dp().toFloat(),
        )
        setPadding(12.dp(), 12.dp(), 12.dp(), 12.dp())
        minimumHeight = 280.dp()
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
        setTextColor(AppColors.Black.toArgb())
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
        setTypeface(typeface, Typeface.BOLD)
        setPadding(10.dp(), 3.dp(), 10.dp(), 3.dp())
        background = roundedDrawable(AdAccent.toArgb(), 4.dp().toFloat())
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        letterSpacing = 0.15f
    }

    val headerSpacer = View(context).apply {
        layoutParams = LinearLayout.LayoutParams(0, 0).apply { weight = 1f }
    }

    val adChoices = CASChoicesView(context).apply {
        layoutParams = LinearLayout.LayoutParams(28.dp(), 28.dp())
    }

    header.addView(adLabel)
    header.addView(headerSpacer)
    header.addView(adChoices)

    val mediaWrapper = FrameLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 12.dp()
        }
        background = roundedDrawable(AppColors.Black.toArgb(), 6.dp().toFloat())
        clipToOutline = true
    }

    val media = CASMediaView(context).apply {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        minimumHeight = 140.dp()
    }

    val icon = ImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(40.dp(), 40.dp()).apply {
            gravity = Gravity.BOTTOM or Gravity.START
            setMargins(8.dp(), 8.dp(), 8.dp(), 8.dp())
        }
        background = roundedDrawable(AppColors.Black.toArgb(), 6.dp().toFloat())
        clipToOutline = true
    }

    mediaWrapper.addView(media)
    mediaWrapper.addView(icon)

    val advertiser = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            topMargin = 12.dp()
        }
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        setTextColor(AdAccent.toArgb())
        setTypeface(typeface, Typeface.NORMAL)
        letterSpacing = 0.05f
    }

    val title = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 4.dp()
        }
        maxLines = 2
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
        setTextColor(AppColors.TextWhite.toArgb())
        setTypeface(typeface, Typeface.NORMAL)
    }

    val body = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 6.dp()
        }
        maxLines = 3
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        setTextColor(BodyTextColor.toArgb())
    }

    val cta = Button(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 44.dp()).apply {
            topMargin = 12.dp()
        }
        isAllCaps = false
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setTextColor(AppColors.Black.toArgb())
        setTypeface(typeface, Typeface.NORMAL)
        background = roundedDrawable(AppColors.TextWhite.toArgb(), 8.dp().toFloat())
        backgroundTintList = ColorStateList.valueOf(AppColors.TextWhite.toArgb())
    }

    container.addView(header)
    container.addView(mediaWrapper)
    container.addView(advertiser)
    container.addView(title)
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
        setPadding(16.dp(), 56.dp(), 16.dp(), 32.dp())
    }

    val header = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    val adLabel = TextView(context).apply {
        text = context.getString(UiR.string.ad)
        setTextColor(AppColors.Black.toArgb())
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        setTypeface(typeface, Typeface.BOLD)
        setPadding(14.dp(), 4.dp(), 14.dp(), 4.dp())
        background = roundedDrawable(AdAccent.toArgb(), 4.dp().toFloat())
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        letterSpacing = 0.18f
    }

    val headerSpacer = View(context).apply {
        layoutParams = LinearLayout.LayoutParams(0, 0).apply { weight = 1f }
    }

    val adChoices = CASChoicesView(context).apply {
        layoutParams = LinearLayout.LayoutParams(32.dp(), 32.dp()).apply {
            marginStart = 8.dp()
        }
    }

    header.addView(adLabel)
    header.addView(adChoices)
    header.addView(headerSpacer)

    val mediaCard = FrameLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 0).apply {
            weight = 1f
            topMargin = 24.dp()
            bottomMargin = 20.dp()
        }
        background = strokedRoundedDrawable(
            fill = AppColors.Black.toArgb(),
            stroke = CardBorder.toArgb(),
            strokeWidthPx = 1.dp(),
            radiusPx = 12.dp().toFloat(),
        )
        clipToOutline = true
    }

    val media = CASMediaView(context).apply {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    val icon = ImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(56.dp(), 56.dp()).apply {
            gravity = Gravity.BOTTOM or Gravity.START
            setMargins(12.dp(), 12.dp(), 12.dp(), 12.dp())
        }
        background = strokedRoundedDrawable(
            fill = AppColors.BackgroundPrimary.toArgb(),
            stroke = CardBorder.toArgb(),
            strokeWidthPx = 1.dp(),
            radiusPx = 8.dp().toFloat(),
        )
        clipToOutline = true
    }

    mediaCard.addView(media)
    mediaCard.addView(icon)

    val advertiser = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        setTextColor(AdAccent.toArgb())
        setTypeface(typeface, Typeface.NORMAL)
        letterSpacing = 0.05f
    }

    val title = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 6.dp()
        }
        maxLines = 2
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        setTextColor(AppColors.TextWhite.toArgb())
        setTypeface(typeface, Typeface.NORMAL)
        setLineSpacing(0f, 1.1f)
    }

    val body = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            topMargin = 10.dp()
            bottomMargin = 24.dp()
        }
        maxLines = 4
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        setTextColor(BodyTextColor.toArgb())
        setLineSpacing(2.dp().toFloat(), 1f)
    }

    val cta = Button(context).apply {
        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 52.dp())
        isAllCaps = false
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setTextColor(AppColors.Black.toArgb())
        setTypeface(typeface, Typeface.NORMAL)
        background = roundedDrawable(AppColors.TextWhite.toArgb(), 10.dp().toFloat())
        backgroundTintList = ColorStateList.valueOf(AppColors.TextWhite.toArgb())
    }

    container.addView(header)
    container.addView(mediaCard)
    container.addView(advertiser)
    container.addView(title)
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

private fun strokedRoundedDrawable(
    fill: Int,
    stroke: Int,
    strokeWidthPx: Int,
    radiusPx: Float,
): GradientDrawable = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = radiusPx
    setColor(fill)
    setStroke(strokeWidthPx, stroke)
}

private fun Int.dp(): Int =
    (this * android.content.res.Resources.getSystem().displayMetrics.density).toInt()

private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
