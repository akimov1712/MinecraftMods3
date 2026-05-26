package dev.akmvxx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.akmvxx.domain.entity.mod.ModEntity
import dev.akmvxx.ui.entity.ScreenUiState

@Composable
fun ModsList(
    modifier: Modifier = Modifier,
    state: LazyListState,
    status: ScreenUiState,
    mods: List<ModEntity>,
    isEndList: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClick: (ModEntity) -> Unit = {},
    adInterval: Int = 0,
    adContent: (@Composable (slotKey: String) -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = 70.dp,
        bottom = 100.dp
    ),
) {
    AppPullRefresh(
        modifier = modifier,
        onRefresh = onRefresh
    ) {
        PaginationList(
            items = mods,
            status = status,
            isEndList = isEndList,
            modifier = Modifier.fillMaxSize(),
            state = state,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = contentPadding,
            onLoadMore = onLoadMore,
            shimmerContent = {
                items(SHIMMER_ITEMS_COUNT) {
                    ModItemShimmer()
                }
            },
        ) { rows ->
            rows.forEachIndexed { index, mod ->
                item(key = "mod-${mod.id}", contentType = MOD_CONTENT_TYPE) {
                    ModItem(mod) { onItemClick(mod) }
                }
                if (adContent != null && adInterval > 0 && (index + 1) % adInterval == 0) {
                    val slot = index / adInterval
                    item(key = "ad-$slot", contentType = AD_CONTENT_TYPE) {
                        adContent("ad-$slot")
                    }
                }
            }
        }
    }
}

private const val SHIMMER_ITEMS_COUNT = 4
private const val MOD_CONTENT_TYPE = "mod"
private const val AD_CONTENT_TYPE = "ad"
