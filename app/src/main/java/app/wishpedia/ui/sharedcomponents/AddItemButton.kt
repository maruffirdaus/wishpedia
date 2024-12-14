package app.wishpedia.ui.sharedcomponents

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import app.wishpedia.R
import app.wishpedia.ui.theme.WishpediaTheme

@Composable
fun AddItemButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(painterResource(R.drawable.ic_add), null)
    }
}

@Preview
@Composable
private fun AddItemButtonPreview() {
    WishpediaTheme(dynamicColor = false) {
        AddItemButton(onClick = {})
    }
}