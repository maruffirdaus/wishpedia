package app.wishpedia.data

import app.wishpedia.data.source.dao.TagDao
import app.wishpedia.data.source.entity.Tag
import app.wishpedia.utils.InitialDataSource

class FakeTagDao : TagDao {
    private var tagData = InitialDataSource.tags.toMutableList()

    override suspend fun insert(tags: List<Tag>) {
        tagData.addAll(tags)
    }

    override suspend fun getTag(id: Int): Tag {
        return tagData.first { it.id == id }
    }

    override suspend fun update(tag: Tag) {
        val oldTag = tagData.first { it.id == tag.id }
        val index = tagData.indexOf(oldTag)
        tagData[index] = tag
    }
}