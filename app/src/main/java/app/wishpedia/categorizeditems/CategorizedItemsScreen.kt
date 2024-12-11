package app.wishpedia.categorizeditems

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import app.wishpedia.R
import app.wishpedia.addeditcategory.AddEditCategoryDialog
import app.wishpedia.addedititem.AddEditItemSheet
import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.itemdetail.ItemDetailPopup
import app.wishpedia.items.AddItemButton
import app.wishpedia.items.AllSection
import app.wishpedia.items.PinnedSection
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.util.DummyDataSource
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizedItemsScreen(
    categoryId: Int,
    onBack: () -> Unit,
    viewModel: CategorizedItemsViewModel = hiltViewModel(),
    hazeState: HazeState = remember { HazeState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.initScreen(categoryId) }
    Scaffold(
        modifier = Modifier
            .haze(hazeState)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            uiState.category?.let { category ->
                CategorizedItemsTopAppBar(
                    category = category,
                    scrollBehavior = scrollBehavior,
                    onNavigationIconClick = onBack,
                    onEditButtonClick = viewModel::showAddEditCategoryDialog,
                    onDeleteButtonClick = viewModel::showDeleteCategoryDialog
                )
            }
        },
        floatingActionButton = {
            AddItemButton(onClick = viewModel::showAddEditItemDialog)
        }
    ) { innerPadding ->
        CategorizedItemsContent(
            pinnedItems = uiState.pinnedItems,
            items = uiState.items,
            loading = uiState.isLoading,
            onItemClick = { itemId ->
                viewModel.showItemDetailPopup(itemId)
            },
            modifier = Modifier.padding(innerPadding)
        )
        if (!uiState.addEditCategoryDialogState.isClosed) {
            AddEditCategoryDialog(
                onDismissRequest = { isEditCategorySucceed ->
                    viewModel.hideAddEditCategoryDialog()
                    if (isEditCategorySucceed) {
                        viewModel.getCategory(categoryId)
                    }
                },
                categoryId = categoryId
            )
        }
        if (!uiState.deleteCategoryDialogState.isClosed) {
            DeleteCategoryDialog(
                onDismissRequest = viewModel::hideDeleteCategoryDialog,
                onConfirmButtonClick = {
                    viewModel.deleteCategory(onSuccess = onBack)
                }
            )
        }
        if (!uiState.addEditItemDialogState.isClosed) {
            uiState.category?.let { category ->
                AddEditItemSheet(
                    onDismissRequest = { isAddItemSucceed ->
                        viewModel.hideAddEditItemDialog()
                        if (isAddItemSucceed) {
                            viewModel.getItems()
                        }
                    },
                    categoryId = category.id,
                )
            }
        }
        if (!uiState.itemDetailDialogState.isClosed) {
            uiState.itemDetailDialogState.itemId?.let { id ->
                ItemDetailPopup(
                    itemId = id,
                    onDismissRequest = { isItemChanged ->
                        viewModel.hideItemDetailPopup()
                        if (isItemChanged) {
                            viewModel.getItems()
                        }
                    },
                    hazeState = hazeState
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizedItemsTopAppBar(
    category: Category,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationIconClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    LargeTopAppBar(
        title = {
            Text(category.name)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(painterResource(R.drawable.ic_arrow_back), null)
            }
        },
        actions = {
            IconButton(
                onClick = onEditButtonClick,
                enabled = category.id != 1
            ) {
                Icon(painterResource(R.drawable.ic_edit), null)
            }
            IconButton(
                onClick = onDeleteButtonClick,
                enabled = category.id != 1
            ) {
                Icon(painterResource(R.drawable.ic_delete), null)
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun CategorizedItemsContent(
    pinnedItems: List<SimplifiedItem>,
    items: List<SimplifiedItem>,
    loading: Boolean,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        items.isNotEmpty() -> {
            AllSection(
                items = items,
                onItemClick = onItemClick,
                modifier = modifier
            ) {
                if (pinnedItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    PinnedSection(
                        items = pinnedItems,
                        onItemClick = onItemClick
                    )
                }
            }
        }

        else -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "No items added yet",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DeleteCategoryDialog(
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
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
                    "Delete category?",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "This category and its items will be deleted",
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
                            onConfirmButtonClick()
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun CategorizedItemsTopAppBarPreview() {
    WishpediaTheme(dynamicColor = false) {
        CategorizedItemsTopAppBar(
            category = DummyDataSource.categories[0],
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            onNavigationIconClick = {},
            onEditButtonClick = {},
            onDeleteButtonClick = {}
        )
    }
}

@Preview
@Composable
private fun CategorizedItemsContentPreview() {
    WishpediaTheme(dynamicColor = false) {
        Surface {
            CategorizedItemsContent(
                pinnedItems = DummyDataSource.items,
                items = DummyDataSource.items,
                loading = false,
                onItemClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun DeleteCategoryDialogPreview() {
    WishpediaTheme(dynamicColor = false) {
        DeleteCategoryDialog(
            onDismissRequest = {},
            onConfirmButtonClick = {}
        )
    }
}