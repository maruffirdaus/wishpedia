package app.wishpedia.ui.sharedcomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource

@Composable
fun ItemsContentSkeleton(
    items: List<SimplifiedItem>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                content()
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "All",
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            ItemCard(
                item = item,
                itemCardColors = ItemCardColors.availableColors[item.cardColorsId],
                onClick = { onItemClick(item.id) },
                modifier = if (index % 2 == 0) {
                    Modifier.padding(start = 24.dp)
                } else {
                    Modifier.padding(end = 24.dp)
                }
            )
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Preview
@Composable
private fun ItemsContentSkeletonPreview() {
    WishpediaTheme(dynamicColor = false) {
        Surface {
            ItemsContentSkeleton(
                items = DummyDataSource.items,
                onItemClick = {}
            ) {}
        }
    }
}