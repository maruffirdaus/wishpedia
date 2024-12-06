package app.wishpedia.data.source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.wishpedia.data.source.entity.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tags: List<Tag>)

    @Query("SELECT * FROM tag WHERE id = :id")
    suspend fun getTag(id: Int): Tag

    @Update
    suspend fun update(tag: Tag)
}