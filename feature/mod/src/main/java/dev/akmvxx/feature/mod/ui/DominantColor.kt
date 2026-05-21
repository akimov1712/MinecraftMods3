package dev.akmvxx.feature.mod.ui

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
internal fun rememberDominantColor(
    url: String?,
    fallback: Color,
): Color {
    val context = LocalContext.current
    var extracted by remember(url) { mutableStateOf<Color?>(null) }

    LaunchedEffect(url) {
        if (url.isNullOrBlank()) return@LaunchedEffect
        val color = withContext(Dispatchers.IO) {
            runCatching {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
                val result = loader.execute(request)
                val drawable = (result as? SuccessResult)?.image?.asDrawable(context.resources)
                    ?: return@runCatching null
                val bitmap: Bitmap = drawable.toBitmap()
                val palette = Palette.from(bitmap).generate()
                val argb = palette.vibrantSwatch?.rgb
                    ?: palette.lightVibrantSwatch?.rgb
                    ?: palette.dominantSwatch?.rgb
                argb?.let { Color(it) }
            }.getOrNull()
        }
        if (color != null) extracted = color
    }

    val animated by animateColorAsState(
        targetValue = extracted ?: fallback,
        animationSpec = tween(durationMillis = 400),
        label = "dominantColor",
    )
    return animated
}
