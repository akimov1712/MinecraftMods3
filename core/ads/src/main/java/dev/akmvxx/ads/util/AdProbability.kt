package dev.akmvxx.ads.util

fun isShowNextAd(valuePercent: Int): Boolean =
    (0 until 100).random() < valuePercent
