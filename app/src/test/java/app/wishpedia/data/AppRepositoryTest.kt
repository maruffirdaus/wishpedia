package app.wishpedia.data

import android.content.Context
import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.ItemTagCrossRef
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class AppRepositoryTest {
    private lateinit var context: Context
    private lateinit var categoryDao: FakeCategoryDao
    private lateinit var itemDao: FakeItemDao
    private lateinit var tagDao: FakeTagDao
    private lateinit var appRepository: AppRepository

    @Before
    fun setUp() {
        context = mock()
        categoryDao = FakeCategoryDao()
        itemDao = FakeItemDao()
        tagDao = FakeTagDao()
        appRepository = AppRepository(context, categoryDao, itemDao, tagDao)
    }

    @Test
    fun `Add Category`() = runTest {
        val category = Category(
            id = 2,
            name = "Example"
        )
        appRepository.addCategory(category)
        assertTrue(categoryDao.getCategories().contains(category))
    }

    @Test
    fun `Delete Category`() = runTest {
        // Add the category first
        val category = Category(
            id = 2,
            name = "Example"
        )
        appRepository.addCategory(category)
        assertTrue(categoryDao.getCategories().contains(category))

        // Then delete the category
        appRepository.deleteCategory(category)
        assertTrue(!categoryDao.getCategories().contains(category))
    }

    @Test
    fun `Add Item`() = runTest {
        val item = Item(
            id = 1,
            name = "Example"
        )
        val itemTagCrossRefs = listOf(
            ItemTagCrossRef(
                itemId = item.id,
                tagId = 0
            ),
            ItemTagCrossRef(
                itemId = item.id,
                tagId = 1
            )
        )
        appRepository.addItem(item, listOf(0, 1))
        assertTrue(itemDao.getItems().contains(item))
        assertTrue(itemDao.getItemTagCrossRefsByItemId(item.id).containsAll(itemTagCrossRefs))
    }

    @Test
    fun `Delete Item`() = runTest {
        // Add the item first
        val item = Item(
            id = 1,
            name = "Example"
        )
        val itemTagCrossRefs = listOf(
            ItemTagCrossRef(
                itemId = item.id,
                tagId = 0
            ),
            ItemTagCrossRef(
                itemId = item.id,
                tagId = 1
            )
        )
        appRepository.addItem(item, listOf(0, 1))
        assertTrue(itemDao.getItems().contains(item))
        assertTrue(itemDao.getItemTagCrossRefsByItemId(item.id).containsAll(itemTagCrossRefs))

        // Then delete the item
        appRepository.deleteItem(item)
        assertTrue(!itemDao.getItems().contains(item))
        assertTrue(!itemDao.getItemTagCrossRefsByItemId(item.id).containsAll(itemTagCrossRefs))
    }

    @Test
    fun `Get Items`() = runTest {
        // Add all of the items first
        val items = listOf(
            Item(
                id = 1,
                name = "Example 1"
            ),
            Item(
                id = 2,
                name = "Example 2"
            ),
            Item(
                id = 3,
                name = "Example 3"
            )
        )
        items.forEach { item ->
            appRepository.addItem(item, listOf(0, 1))
        }

        // Then check if it can return the same items
        assertEquals(items, appRepository.getItems())
    }
}