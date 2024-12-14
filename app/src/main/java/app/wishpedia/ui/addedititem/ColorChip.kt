package app.wishpedia.ui.addedititem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.wishpedia.R
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme

@Composable
fun ColorChip(
    colors: ItemCardColors,
    onClick: () -> Unit,
    selected: Boolean = false,
    enabled: Boolean = true
) {
    Box(modifier = Modifier.padding(4.dp)) {
        val modifier = Modifier
            .width(40.dp)
            .aspectRatio(1.0f)
            .clip(CircleShape)
        Box(
            modifier = if (enabled) {
                modifier
                    .background(colors.containerColor)
                    .clickable { onClick() }
            } else {
                modifier.background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12F))
            },
            contentAlignment = Alignment.Center
        ) {
            if (selected) Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = if (enabled) {
                    colors.contentColor
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38F)
                }
            )
        }
    }
}

@Preview
@Composable
private fun ColorChipPreview() {
    WishpediaTheme(dynamicColor = false) {
        ColorChip(
            colors = ItemCardColors.availableColors[0],
            onClick = {}
        )
    }
}