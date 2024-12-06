package app.wishpedia.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryId: Int = 1,
    var cardColorsId: Int = 0,
    var name: String,
    var description: String? = null,
    var image: String? = null,
    var price: Int? = null,
    var link: String? = null,
    var priorityPoint: Int = 0,
    var isPinned: Boolean = false,
    var isMarkedAsDone: Boolean = false
)