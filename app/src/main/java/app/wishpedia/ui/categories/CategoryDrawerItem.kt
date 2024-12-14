package app.wishpedia.ui.categories

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import app.wishpedia.R
import app.wishpedia.data.source.entity.Category
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.DummyDataSource

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
fun CategoryDrawerItemPreview() {
    WishpediaTheme(dynamicColor = false) {
        Surface {
            CategoryDrawerItem(
                category = DummyDataSource.categories[0],
                onCategoryClick = {}
            )
        }
    }
}