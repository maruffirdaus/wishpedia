package app.wishpedia.data.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import app.wishpedia.data.source.entity.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Query("SELECT * FROM category ORDER BY id DESC")
    suspend fun getCategories(): List<Category>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategory(id: Int): Category

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)
}