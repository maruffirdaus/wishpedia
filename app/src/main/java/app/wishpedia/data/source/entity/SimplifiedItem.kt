package app.wishpedia.data.source.entity

import androidx.compose.runtime.Immutable

@Immutable
data class SimplifiedItem(
    val id: Int,
    val cardColorsId: Int,
    val name: String,
    val description: String? = null,
    val price: Int? = null
)