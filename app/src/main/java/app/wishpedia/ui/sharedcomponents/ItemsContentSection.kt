package app.wishpedia.ui.sharedcomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
fun ItemsContentSection(
    title: String,
    items: List<SimplifiedItem>,
    onItemClick: (Int) -> Unit
) {
    Column {
        Text(
            title,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            state = rememberLazyListState(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.width(8.dp))
            }
            items(items, key = { item -> item.id }) { item ->
                ItemCard(
                    item = item,
                    itemCardColors = ItemCardColors.availableColors[item.cardColorsId],
                    onClick = { onItemClick(item.id) }
                )
            }
            item {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ItemsContentSectionPreview() {
    WishpediaTheme(dynamicColor = false) {
        Surface {
            ItemsContentSection(
                title = "Pinned",
                items = DummyDataSource.items,
                onItemClick = {},
            )
        }
    }
}