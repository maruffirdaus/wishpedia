package app.wishpedia.ui.itemdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import app.wishpedia.R
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.Tag
import app.wishpedia.ui.addedititem.AddEditItemSheet
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@Composable
fun ItemDetailPopup(
    itemId: Int,
    onDismissRequest: (Boolean) -> Unit,
    viewModel: ItemDetailViewModel = hiltViewModel(),
    hazeState: HazeState = remember { HazeState() }
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.getItem(itemId) }
    Popup(
        onDismissRequest = {
            onDismissRequest(uiState.isItemChanged)
            viewModel.resetUiState()
        },
        properties = PopupProperties(focusable = true)
    ) {
        uiState.item?.let { item ->
            uiState.tags?.let { tags ->
                ItemDetailContent(
                    item = item,
                    tags = tags,
                    loading = uiState.isLoading,
                    onMarkButtonClick = viewModel::showMarkItemAsDoneDialog,
                    onPinButtonClick = viewModel::updateItemPinnedState,
                    onEditButtonClick = viewModel::showAddEditItemDialog,
                    onDeleteButtonClick = viewModel::showDeleteItemDialog,
                    onDismissRequest = {
                        onDismissRequest(uiState.isItemChanged)
                        viewModel.resetUiState()
                    },
                    hazeState = hazeState
                )
            }
        }
    }
    if (!uiState.markItemAsDoneDialogState.isClosed) {
        MarkItemAsDoneDialog(
            onDismissRequest = viewModel::hideMarkItemAsDoneDialog,
            onConfirmButtonClick = {
                viewModel.markItemAsDone(
                    onSuccess = { onDismissRequest(true) }
                )
                viewModel.resetUiState()
            }
        )
    }
    if (!uiState.addEditItemDialogState.isClosed) {
        AddEditItemSheet(
            onDismissRequest = { isEditItemSuccessful ->
                viewModel.hideAddEditItemDialog()
                if (isEditItemSuccessful) {
                    viewModel.refreshItem()
                }
            },
            itemId = uiState.item?.id
        )
    }
    if (!uiState.deleteItemDialogState.isClosed) {
        DeleteItemDialog(
            onDismissRequest = viewModel::hideDeleteItemDialog,
            onConfirmButtonClick = {
                viewModel.deleteItem(
                    onSuccess = { onDismissRequest(true) }
                )
                viewModel.resetUiState()
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
    onMarkButtonClick: () -> Unit,
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
                    Box {
                        if (!item.isMarkedAsDone) {
                            FilledIconButton(
                                onClick = onMarkButtonClick
                            ) {
                                Icon(painterResource(R.drawable.ic_check), null)
                            }
                        }
                    }
                    Row {
                        if (!item.isMarkedAsDone) {
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
                    itemCardColors = if (!item.isMarkedAsDone) {
                        ItemCardColors.availableColors[item.cardColorsId]
                    } else {
                        ItemCardColors.getDoneColors()
                    },
                    modifier = Modifier.padding(horizontal = 36.dp)
                )
                Spacer(modifier = Modifier.height(36.dp))
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
            onMarkButtonClick = {},
            onPinButtonClick = {},
            onEditButtonClick = {},
            onDeleteButtonClick = {},
            onDismissRequest = {}
        )
    }
}