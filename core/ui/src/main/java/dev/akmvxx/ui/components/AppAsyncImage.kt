package dev.akmvxx.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.valentinilk.shimmer.shimmer
import dev.akmvxx.ui.AppColors

@Composable
fun AppAsyncImage(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
) {
    val context = LocalContext.current
    val request = remember(url) {
        ImageRequest.Builder(context)
            .data(url)
            .crossfade(false)
            .memoryCacheKey(url)
            .diskCacheKey(url)
            .build()
    }

    var isLoad by remember { mutableStateOf(true) }
    val loaderModifier = if (isLoad) Modifier.shimmer().background(AppColors.Shimmer) else Modifier.background(AppColors.BackgroundSecondary)
    AsyncImage(
        modifier = modifier
            .then(loaderModifier)
            ,
        model = request,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
        onState = {
            isLoad = it !is AsyncImagePainter.State.Success
            if (it is AsyncImagePainter.State.Error) {
                Log.e("Async Image", it.result.throwable.message ?: "???")
            }
        },
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        clipToBounds = clipToBounds,

    )
}
