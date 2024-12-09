package app.wishpedia.categories

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import app.wishpedia.R
import app.wishpedia.addeditcategory.AddEditCategoryDialog
import app.wishpedia.data.source.entity.Category
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.util.DummyDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CategoryDrawer(
    onCategoryClick: (Int) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    scope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (() -> Unit) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(Unit) {
        if (lifecycleState == Lifecycle.State.STARTED || lifecycleState == Lifecycle.State.RESUMED) {
            viewModel.getCategories()
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CategoryDrawerSheet(
                categories = uiState.categories,
                onCategoryClick = onCategoryClick,
                onAddButtonClick = viewModel::showAddEditCategoryDialog
            )
        },
        content = {
            content {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
            if (!uiState.addEditCategoryDialogState.isClosed) {
                AddEditCategoryDialog(
                    onDismissRequest = { isAddCategorySucceed ->
                        viewModel.hideAddEditCategoryDialog()
                        if (isAddCategorySucceed) {
                            viewModel.getCategories()
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun CategoryDrawerSheet(
    categories: List<Category>,
    onCategoryClick: (Int) -> Unit,
    onAddButtonClick: () -> Unit
) {
    ModalDrawerSheet {
        LazyColumn(modifier = Modifier.padding(12.dp)) {
            item {
                Text(
                    "Category",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall
                )
            }
            items(categories, key = { category -> category.id }) { category ->
                CategoryDrawerItem(
                    category = category,
                    onCategoryClick = onCategoryClick
                )
            }
            item {
                NavigationDrawerItem(
                    label = {
                        Text(
                            "Add",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    selected = false,
                    onClick = onAddButtonClick,
                    icon = {
                        Icon(painterResource(R.drawable.ic_add), null)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryDrawerItem(
    category: Category,
    onCategoryClick: (Int) -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                category.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge
            )
        },
        selected = false,
        onClick = { onCategoryClick(category.id) },
        icon = {
            Icon(painterResource(R.drawable.ic_folder), null)
        }
    )
}

@Preview
@Composable
private fun CategoryDrawerSheetPreview() {
    WishpediaTheme(dynamicColor = false) {
        CategoryDrawerSheet(
            categories = DummyDataSource.categories,
            onCategoryClick = {},
            onAddButtonClick = {}
        )
    }
}