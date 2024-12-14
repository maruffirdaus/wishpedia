package app.wishpedia.ui.categorizeditems

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import app.wishpedia.R
import app.wishpedia.data.source.entity.Category
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource

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