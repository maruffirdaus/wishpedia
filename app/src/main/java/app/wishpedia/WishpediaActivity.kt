package app.wishpedia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import app.wishpedia.ui.theme.WishpediaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishpediaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WishpediaTheme(dynamicColor = false) {
                WishpediaNavGraph()
            }
        }
    }
}