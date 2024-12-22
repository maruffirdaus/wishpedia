package app.wishpedia.data

import app.wishpedia.data.source.dao.ItemDao
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.ItemTagCrossRef
import app.wishpedia.data.source.entity.ItemWithTags

class FakeItemDao : ItemDao {
    private var itemData = mutableListOf<Item>()
    private var itemTagCrossRefData = mutableListOf<ItemTagCrossRef>()

    override suspend fun insert(item: Item): Long {
        itemData.add(item)
        return item.id.toLong()
    }

    override suspend fun insert(itemTagCrossRef: ItemTagCrossRef) {
        itemTagCrossRefData.add(itemTagCrossRef)
    }

    override suspend fun getHighestPriorityItems(limit: Int): List<Item> {
        return itemData.sortedByDescending { it.priorityPoint }
    }

    override suspend fun getPinnedItems(): List<Item> {
        return itemData.filter { it.isPinned }
    }

    override suspend fun getPinnedItems(categoryId: Int): List<Item> {
        return itemData.filter { it.categoryId == categoryId && it.isPinned }
    }

    override suspend fun getItems(): List<Item> {
        return itemData
    }

    override suspend fun getItems(categoryId: Int): List<Item> {
        return itemData.filter { it.categoryId == categoryId }
    }

    override suspend fun getDoneItems(): List<Item> {
        return itemData.filter { it.isMarkedAsDone }
    }

    override suspend fun getDoneItems(categoryId: Int): List<Item> {
        return itemData.filter { it.categoryId == categoryId && it.isMarkedAsDone }
    }

    override suspend fun getItem(id: Int): Item {
        return itemData.first { it.id == id }
    }

    override suspend fun getItemWithTags(id: Int): ItemWithTags {
        return ItemWithTags(
            item = itemData.first { it.id == id },
            tags = listOf()
        )
    }

    override suspend fun getItemTagCrossRefsByItemId(id: Int): List<ItemTagCrossRef> {
        return itemTagCrossRefData.filter { it.itemId == id }
    }

    override suspend fun getItemTagCrossRefsByTagId(id: Int): List<ItemTagCrossRef> {
        return itemTagCrossRefData.filter { it.tagId == id }
    }

    override suspend fun update(item: Item) {
        val oldItem = itemData.first { it.id == item.id }
        val index = itemData.indexOf(oldItem)
        itemData[index] = item
    }

    override suspend fun delete(item: Item) {
        itemData.remove(item)
    }

    override suspend fun delete(itemTagCrossRef: ItemTagCrossRef) {
        itemTagCrossRefData.remove(itemTagCrossRef)
    }
}