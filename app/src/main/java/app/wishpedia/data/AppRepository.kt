package app.wishpedia.data

import android.content.Context
import androidx.core.net.toUri
import app.wishpedia.data.source.dao.CategoryDao
import app.wishpedia.data.source.dao.ItemDao
import app.wishpedia.data.source.dao.TagDao
import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.ItemTagCrossRef
import app.wishpedia.utils.ImageUtils
import java.io.File

class AppRepository(
    private val context: Context,
    private val categoryDao: CategoryDao,
    private val itemDao: ItemDao,
    private val tagDao: TagDao
) {
    suspend fun addCategory(category: Category) = categoryDao.insert(category)

    suspend fun addItem(item: Item, tagIds: List<Int>) {
        var priorityPoint = 0
        item.image?.let { image ->
            item.image = ImageUtils.storeImage(context, image.toUri()).toString()
        }
        tagIds.forEach { tagId ->
            val tag = tagDao.getTag(tagId)
            priorityPoint += tag.point
        }
        priorityPoint /= tagIds.size
        item.priorityPoint = priorityPoint
        val itemId = itemDao.insert(item)
        tagIds.forEach { tagId ->
            itemDao.insert(ItemTagCrossRef(itemId.toInt(), tagId))
        }
    }

    suspend fun getCategories(): List<Category> = categoryDao.getCategories()

    suspend fun getCategory(id: Int): Category = categoryDao.getCategory(id)

    suspend fun getFeaturedItems(limit: Int = 5) = itemDao.getHighestPriorityItems(limit)

    suspend fun getPinnedItems() = itemDao.getPinnedItems()

    suspend fun getPinnedItems(categoryId: Int) = itemDao.getPinnedItems(categoryId)

    suspend fun getItems() = itemDao.getItems()

    suspend fun getItems(categoryId: Int) = itemDao.getItems(categoryId)

    suspend fun getItem(id: Int) = itemDao.getItem(id)

    suspend fun getItemWithTags(id: Int) = itemDao.getItemWithTags(id)

    suspend fun updateCategory(category: Category) = categoryDao.update(category)

    suspend fun updateItem(item: Item, tagIds: List<Int>) {
        val itemId = item.id
        val oldItem = itemDao.getItem(itemId)
        val oldItemTagCrossRefs = itemDao.getItemTagCrossRefs(itemId)
        var priorityPoint = 0
        if (item.image != oldItem.image) {
            oldItem.image?.toUri()?.path?.let {
                File(it).delete()
            }
        }
        item.image?.let { image ->
            item.image = ImageUtils.storeImage(context, image.toUri()).toString()
        }
        tagIds.forEach { tagId ->
            val tag = tagDao.getTag(tagId)
            priorityPoint += tag.point
        }
        priorityPoint /= tagIds.size
        item.priorityPoint = priorityPoint
        oldItemTagCrossRefs.forEach {
            itemDao.delete(it)
        }
        itemDao.update(item)
        tagIds.forEach { tagId ->
            itemDao.insert(ItemTagCrossRef(itemId, tagId))
        }
    }

    suspend fun updateItemPinnedState(item: Item): Item {
        item.isPinned = !item.isPinned
        itemDao.update(item)
        return item
    }

    suspend fun deleteCategory(category: Category) {
        itemDao.getItems(category.id).forEach { item ->
            deleteItem(item)
        }
        categoryDao.delete(category)
    }

    suspend fun deleteItem(item: Item) {
        item.image?.toUri()?.path?.let {
            File(it).delete()
        }
        itemDao.getItemTagCrossRefs(item.id).forEach { itemTagCrossRef ->
            itemDao.delete(itemTagCrossRef)
        }
        itemDao.delete(item)
    }
}