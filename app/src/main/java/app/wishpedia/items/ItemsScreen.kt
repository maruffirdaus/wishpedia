package app.wishpedia.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.wishpedia.R
import app.wishpedia.addedititem.AddEditItemSheet
import app.wishpedia.categories.CategoryDrawer
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.itemdetail.ItemDetailPopup
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.util.DummyDataSource
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import java.text.NumberFormat
import java.util.Locale

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsTopAppBar(
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick
            ) {
                Icon(painterResource(R.drawable.ic_menu), null)
            }
        },
        scrollBehavior = scrollBehavior
    )
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
            AllSection(
                items = items,
                onItemClick = onItemClick,
                modifier = modifier
            ) {
                FeaturedSection(
                    items = featuredItems,
                    onItemClick = onItemClick
                )
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
fun FeaturedSection(
    items: List<SimplifiedItem>,
    onItemClick: (Int) -> Unit
) {
    Text(
        "Featured",
        modifier = Modifier.padding(horizontal = 24.dp),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium
    )
    Text(
        "Recommended items for you",
        modifier = Modifier.padding(horizontal = 24.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
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

@Composable
fun PinnedSection(
    items: List<SimplifiedItem>,
    onItemClick: (Int) -> Unit
) {
    Text(
        "Pinned",
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

@Composable
fun AllSection(
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

@Composable
fun AddItemButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(painterResource(R.drawable.ic_add), null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ItemsTopAppBarPreview() {
    WishpediaTheme(dynamicColor = false) {
        ItemsTopAppBar(
            onNavigationIconClick = {},
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        )
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

@Preview
@Composable
private fun AddItemButtonPreview() {
    WishpediaTheme(dynamicColor = false) {
        AddItemButton(onClick = {})
    }
}