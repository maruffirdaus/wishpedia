package app.wishpedia.data.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.ItemTagCrossRef
import app.wishpedia.data.source.entity.ItemWithTags

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemTagCrossRef: ItemTagCrossRef)

    @Query("SELECT * FROM item WHERE isMarkedAsDone = FALSE ORDER BY priorityPoint DESC, id DESC LIMIT :limit")
    suspend fun getHighestPriorityItems(limit: Int): List<Item>

    @Query("SELECT * FROM item WHERE isPinned = true AND isMarkedAsDone = FALSE ORDER BY id DESC")
    suspend fun getPinnedItems(): List<Item>

    @Query("SELECT * FROM item WHERE categoryId = :categoryId AND isPinned = true AND isMarkedAsDone = FALSE ORDER BY id DESC")
    suspend fun getPinnedItems(categoryId: Int): List<Item>

    @Query("SELECT * FROM item WHERE isMarkedAsDone = FALSE ORDER BY id DESC")
    suspend fun getItems(): List<Item>

    @Query("SELECT * FROM item WHERE categoryId = :categoryId AND isMarkedAsDone = FALSE ORDER BY id DESC")
    suspend fun getItems(categoryId: Int): List<Item>

    @Query("SELECT * FROM item WHERE isMarkedAsDone = true ORDER BY id DESC")
    suspend fun getDoneItems(): List<Item>

    @Query("SELECT * FROM item WHERE categoryId = :categoryId AND isMarkedAsDone = true ORDER BY id DESC")
    suspend fun getDoneItems(categoryId: Int): List<Item>

    @Query("SELECT * FROM item WHERE id = :id")
    suspend fun getItem(id: Int): Item

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id")
    suspend fun getItemWithTags(id: Int): ItemWithTags

    @Query("SELECT * FROM itemtagcrossref WHERE itemId = :id")
    suspend fun getItemTagCrossRefsByItemId(id: Int): List<ItemTagCrossRef>

    @Query("SELECT * FROM itemtagcrossref WHERE tagId = :id")
    suspend fun getItemTagCrossRefsByTagId(id: Int): List<ItemTagCrossRef>

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Delete
    suspend fun delete(itemTagCrossRef: ItemTagCrossRef)
}