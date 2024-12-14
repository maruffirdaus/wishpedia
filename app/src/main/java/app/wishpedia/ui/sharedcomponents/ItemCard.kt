package app.wishpedia.ui.sharedcomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ItemCard(
    item: SimplifiedItem,
    itemCardColors: ItemCardColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val size = (LocalConfiguration.current.screenWidthDp.dp - 64.dp) / 2
    Card(
        modifier = modifier.size(size),
        colors = CardDefaults.cardColors(
            containerColor = itemCardColors.containerColor,
            contentColor = itemCardColors.contentColor
        )
    ) {
        Box(modifier = Modifier.clickable { onClick() }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val contentColorVariant = itemCardColors.contentColorVariant
                Column {
                    Text(
                        item.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    item.description?.let { description ->
                        Text(
                            description,
                            color = contentColorVariant,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                item.price?.let { price ->
                    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        buildString {
                            append("Rp")
                            append(numberFormat.format(price))
                        },
                        color = contentColorVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ItemCardPreview() {
    WishpediaTheme(dynamicColor = false) {
        ItemCard(
            item = DummyDataSource.items[0],
            itemCardColors = ItemCardColors.availableColors[0],
            onClick = {}
        )
    }
}