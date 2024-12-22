package app.wishpedia.data.source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tag(
    @PrimaryKey
    val id: Int,
    val name: String,
    var point: Int
)