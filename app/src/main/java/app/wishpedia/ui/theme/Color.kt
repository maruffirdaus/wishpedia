package app.wishpedia.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val primaryDark = Color(0xFFFFDFB3)
val onPrimaryDark = Color(0xFF432C00)
val primaryContainerDark = Color(0xFFE2B268)
val onPrimaryContainerDark = Color(0xFF3F2900)
val secondaryDark = Color(0xFFDDC39E)
val onSecondaryDark = Color(0xFF3E2E14)
val secondaryContainerDark = Color(0xFF4C3B20)
val onSecondaryContainerDark = Color(0xFFE8CDA8)
val tertiaryDark = Color(0xFFDDEB9E)
val onTertiaryDark = Color(0xFF2B3400)
val tertiaryContainerDark = Color(0xFFB3C178)
val onTertiaryContainerDark = Color(0xFF283100)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF16130E)
val onBackgroundDark = Color(0xFFEAE1D8)
val surfaceDark = Color(0xFF16130E)
val onSurfaceDark = Color(0xFFEAE1D8)
val surfaceVariantDark = Color(0xFF4F4538)
val onSurfaceVariantDark = Color(0xFFD3C4B3)
val outlineDark = Color(0xFF9B8F7F)
val outlineVariantDark = Color(0xFF4F4538)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFEAE1D8)
val inverseOnSurfaceDark = Color(0xFF34302A)
val inversePrimaryDark = Color(0xFF7C5815)
val surfaceDimDark = Color(0xFF16130E)
val surfaceBrightDark = Color(0xFF3D3832)
val surfaceContainerLowestDark = Color(0xFF110E09)
val surfaceContainerLowDark = Color(0xFF1F1B16)
val surfaceContainerDark = Color(0xFF231F1A)
val surfaceContainerHighDark = Color(0xFF2E2924)
val surfaceContainerHighestDark = Color(0xFF39342E)

val primaryLight = Color(0xFF7C5815)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFF1C074)
val onPrimaryContainerLight = Color(0xFF4C3200)
val secondaryLight = Color(0xFF6F5B3E)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFCE0BA)
val onSecondaryContainerLight = Color(0xFF58462A)
val tertiaryLight = Color(0xFF586425)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFC1CF84)
val onTertiaryContainerLight = Color(0xFF313B00)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFFFF8F3)
val onBackgroundLight = Color(0xFF1F1B16)
val surfaceLight = Color(0xFFFFF8F3)
val onSurfaceLight = Color(0xFF1F1B16)
val surfaceVariantLight = Color(0xFFEFE0CF)
val onSurfaceVariantLight = Color(0xFF4F4538)
val outlineLight = Color(0xFF817567)
val outlineVariantLight = Color(0xFFD3C4B3)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF34302A)
val inverseOnSurfaceLight = Color(0xFFF9EFE6)
val inversePrimaryLight = Color(0xFFEFBF73)
val surfaceDimLight = Color(0xFFE2D8D0)
val surfaceBrightLight = Color(0xFFFFF8F3)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFFCF2E9)
val surfaceContainerLight = Color(0xFFF6ECE3)
val surfaceContainerHighLight = Color(0xFFF0E7DE)
val surfaceContainerHighestLight = Color(0xFFEAE1D8)

@Immutable
data class ItemCardColors(
    val id: Int,
    val containerColor: Color,
    val contentColor: Color,
    val contentColorVariant: Color
) {
    companion object {
        val availableColors = listOf(
            ItemCardColors(
                0,
                Color(0xFFFFDDAE),
                Color(0xFF281800),
                Color(0xFF604100)
            ),
            ItemCardColors(
                1,
                Color(0xFFFADEB9),
                Color(0xFF271903),
                Color(0xFF564428)
            ),
            ItemCardColors(
                2,
                Color(0xFFDBEA9C),
                Color(0xFF181E00),
                Color(0xFF404B0F)
            )
        )
    }
}