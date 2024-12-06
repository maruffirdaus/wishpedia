package app.wishpedia.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["itemId", "tagId"])
data class ItemTagCrossRef(
    val itemId: Int,
    @ColumnInfo(index = true)
    val tagId: Int
)