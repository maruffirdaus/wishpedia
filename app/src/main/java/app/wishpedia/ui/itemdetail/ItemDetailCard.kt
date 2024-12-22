package app.wishpedia.ui.itemdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.Tag
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemDetailCard(
    item: Item,
    tags: List<Tag>,
    itemCardColors: ItemCardColors,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = itemCardColors.containerColor,
            contentColor = itemCardColors.contentColor
        )
    ) {
        val minHeight = LocalConfiguration.current.screenWidthDp.minus(72).div(0.75F).dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = minHeight
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val contentColorVariant = itemCardColors.contentColorVariant
            Column {
                Text(
                    item.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyLarge
                )
                item.description?.let { description ->
                    Text(
                        description,
                        color = contentColorVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    tags.joinToString { it.name },
                    color = contentColorVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium
                )
                item.image?.let { image ->
                    Spacer(modifier = Modifier.height(24.dp))
                    GlideImage(
                        model = image,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.0f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item.price?.let { price ->
                    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                    Text(
                        buildString {
                            append("Rp")
                            append(numberFormat.format(price))
                        },
                        modifier = Modifier.weight(1.0f),
                        color = contentColorVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                item.link?.let { link ->
                    val uriHandler = LocalUriHandler.current
                    Text(
                        link,
                        modifier = Modifier
                            .clickable {
                                uriHandler.openUri(
                                    if (!link.startsWith("http://") && !link.startsWith("https://")) "http://$link" else link
                                )
                            },
                        color = contentColorVariant,
                        textAlign = TextAlign.End,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelMedium.copy(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ItemDetailCardPreview() {
    WishpediaTheme(dynamicColor = false) {
        ItemDetailCard(
            item = DummyDataSource.item,
            tags = DummyDataSource.tags,
            itemCardColors = ItemCardColors.availableColors[0]
        )
    }
}