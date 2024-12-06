package app.wishpedia.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import app.wishpedia.data.source.dao.CategoryDao
import app.wishpedia.data.source.dao.ItemDao
import app.wishpedia.data.source.dao.TagDao
import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.ItemTagCrossRef
import app.wishpedia.data.source.entity.Tag

@Database(
    entities = [Category::class, Item::class, Tag::class, ItemTagCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao
    abstract fun tagDao(): TagDao
}