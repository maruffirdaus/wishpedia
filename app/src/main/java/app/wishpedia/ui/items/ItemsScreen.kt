package app.wishpedia.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.ui.addedititem.AddEditItemSheet
import app.wishpedia.ui.categories.CategoryDrawer
import app.wishpedia.ui.itemdetail.ItemDetailPopup
import app.wishpedia.ui.sharedcomponents.AddItemButton
import app.wishpedia.ui.sharedcomponents.ItemsContentSection
import app.wishpedia.ui.sharedcomponents.ItemsContentSkeleton
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    onCategoryClick: (Int) -> Unit,
    viewModel: ItemsViewModel = hiltViewModel(),
    hazeState: HazeState = remember { HazeState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) {
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(lifecycleState) {
        if (lifecycleState == Lifecycle.State.STARTED || lifecycleState == Lifecycle.State.RESUMED) {
            viewModel.getItems()
        }
    }
    CategoryDrawer(onCategoryClick = onCategoryClick) {
        Scaffold(
            modifier = Modifier
                .haze(hazeState)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ItemsTopAppBar(
                    onNavigationIconClick = it,
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                AddItemButton(onClick = viewModel::showAddEditItemDialog)
            }
        ) { innerPadding ->
            val uiState by viewModel.uiState.collectAsState()
            ItemsContent(
                featuredItems = uiState.featuredItems,
                pinnedItems = uiState.pinnedItems,
                items = uiState.items,
                loading = uiState.isLoading,
                onItemClick = { itemId ->
                    viewModel.showItemDetailPopup(itemId)
                },
                modifier = Modifier.padding(innerPadding)
            )
            if (!uiState.addEditItemDialogState.isClosed) {
                AddEditItemSheet(
                    onDismissRequest = { isAddItemSucceed ->
                        viewModel.hideAddEditItemDialog()
                        if (isAddItemSucceed) {
                            viewModel.getItems()
                        }
                    }
                )
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
}

@Composable
fun ItemsContent(
    featuredItems: List<SimplifiedItem>,
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
            ItemsContentSkeleton(
                items = items,
                onItemClick = onItemClick,
                modifier = modifier
            ) {
                ItemsContentSection(
                    title = "Featured",
                    items = featuredItems,
                    onItemClick = onItemClick
                )
                if (pinnedItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    ItemsContentSection(
                        title = "Pinned",
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

@Preview
@Composable
private fun ItemsContentPreview() {
    WishpediaTheme(dynamicColor = false) {
        Surface {
            ItemsContent(
                featuredItems = DummyDataSource.items,
                pinnedItems = DummyDataSource.items,
                items = DummyDataSource.items,
                loading = false,
                onItemClick = {}
            )
        }
    }
}