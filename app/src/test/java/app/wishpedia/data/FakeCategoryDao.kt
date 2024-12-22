package app.wishpedia.data

import app.wishpedia.data.source.dao.CategoryDao
import app.wishpedia.data.source.entity.Category
import app.wishpedia.utils.InitialDataSource

class FakeCategoryDao : CategoryDao {
    private var categoryData = mutableListOf(InitialDataSource.category)

    override suspend fun insert(category: Category) {
        categoryData.add(category)
    }

    override suspend fun getCategories(): List<Category> {
        return categoryData
    }

    override suspend fun getCategory(id: Int): Category {
        return categoryData.first { it.id == id }
    }

    override suspend fun update(category: Category) {
        val oldCategory = categoryData.first { it.id == category.id }
        val index = categoryData.indexOf(oldCategory)
        categoryData[index] = category
    }

    override suspend fun delete(category: Category) {
        categoryData.remove(category)
    }
}