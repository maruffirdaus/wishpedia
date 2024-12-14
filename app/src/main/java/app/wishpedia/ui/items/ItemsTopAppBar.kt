package app.wishpedia.ui.items

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.wishpedia.R
import app.wishpedia.ui.theme.WishpediaTheme

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