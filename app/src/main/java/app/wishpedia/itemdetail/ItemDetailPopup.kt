package app.wishpedia.itemdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import app.wishpedia.R
import app.wishpedia.addedititem.AddEditItemSheet
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.Tag
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.util.DummyDataSource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ItemDetailPopup(
    itemId: Int,
    onDismissRequest: () -> Unit,
    onItemChange: () -> Unit,
    viewModel: ItemDetailViewModel = hiltViewModel(),
    hazeState: HazeState = remember { HazeState() },
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.getItem(itemId) }
    Popup(
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        uiState.item?.let { item ->
            uiState.tags?.let { tags ->
                ItemDetailContent(
                    item = item,
                    tags = tags,
                    loading = uiState.isLoading,
                    onPinButtonClick = {
                        scope.launch {
                            viewModel.updateItemPinnedState()
                            onItemChange()
                        }
                    },
                    onEditButtonClick = viewModel::showAddEditItemDialog,
                    onDeleteButtonClick = viewModel::showDeleteItemDialog,
                    onDismissRequest = onDismissRequest,
                    hazeState = hazeState
                )
            }
        }
    }
    if (!uiState.addEditItemDialogState.isClosed) {
        AddEditItemSheet(
            onDismissRequest = viewModel::hideAddEditItemDialog,
            onConfirmRequest = {
                viewModel.refreshItem()
                onItemChange()
            },
            itemId = uiState.item?.id
        )
    }
    if (!uiState.deleteItemDialogState.isClosed) {
        DeleteItemDialog(
            onDismissRequest = viewModel::hideDeleteItemDialog,
            onConfirmRequest = {
                scope.launch {
                    viewModel.deleteItem()
                    onDismissRequest()
                    onItemChange()
                }
            }
        )
    }
}

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun ItemDetailContent(
    item: Item,
    tags: List<Tag>,
    loading: Boolean,
    onPinButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    hazeState: HazeState = remember { HazeState() }
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .hazeChild(
                state = hazeState,
                style = HazeMaterials.thin()
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onDismissRequest()
            },
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column {
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilledIconButton(
                        onClick = {}
                    ) {
                        Icon(painterResource(R.drawable.ic_check), null)
                    }
                    Row {
                        FilledTonalIconButton(
                            onClick = onPinButtonClick
                        ) {
                            Icon(
                                painterResource(
                                    if (item.isPinned) R.drawable.ic_keep_off else R.drawable.ic_keep
                                ),
                                null
                            )
                        }
                        FilledTonalIconButton(
                            onClick = onEditButtonClick
                        ) {
                            Icon(painterResource(R.drawable.ic_edit), null)
                        }
                        FilledTonalIconButton(
                            onClick = onDeleteButtonClick
                        ) {
                            Icon(painterResource(R.drawable.ic_delete), null)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ItemDetailCard(
                    item = item,
                    tags = tags,
                    itemCardColors = ItemCardColors.availableColors[item.cardColorsId],
                    modifier = Modifier.padding(horizontal = 36.dp)
                )
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

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
                item.description?.let {
                    Text(
                        it,
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
                item.price?.let {
                    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
                    Text(
                        buildString {
                            append("Rp")
                            append(numberFormat.format(it))
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

@Composable
fun DeleteItemDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Delete item?",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "This item will be deleted",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirmRequest()
                            onDismissRequest()
                        }
                    ) {
                        Text("Confirm")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ItemDetailContentPreview() {
    WishpediaTheme(dynamicColor = false) {
        ItemDetailContent(
            item = DummyDataSource.item,
            tags = DummyDataSource.tags,
            loading = false,
            onPinButtonClick = {},
            onEditButtonClick = {},
            onDeleteButtonClick = {},
            onDismissRequest = {}
        )
    }
}

@Preview
@Composable
private fun DeleteItemDialogPreview() {
    WishpediaTheme(dynamicColor = false) {
        DeleteItemDialog(
            onDismissRequest = {},
            onConfirmRequest = {}
        )
    }
}