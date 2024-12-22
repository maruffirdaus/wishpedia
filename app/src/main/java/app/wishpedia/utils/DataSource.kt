package app.wishpedia.utils

import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.data.source.entity.Tag

object InitialDataSource {
    val category = Category(1, "Uncategorized")
    val tags = listOf(
        Tag(id = 0, name = "Other", point = 10),
        Tag(id = 1, name = "Work", point = 100),
        Tag(id = 2, name = "Education", point = 100),
        Tag(id = 3, name = "Fitness", point = 80),
        Tag(id = 4, name = "Hobby", point = 60),
        Tag(id = 5, name = "Travel", point = 60),
        Tag(id = 6, name = "Photography", point = 60),
        Tag(id = 7, name = "Creativity", point = 60),
        Tag(id = 8, name = "Reading", point = 60),
        Tag(id = 9, name = "Movies", point = 60),
        Tag(id = 10, name = "Writing", point = 60),
        Tag(id = 11, name = "Game", point = 60),
        Tag(id = 12, name = "Cooking", point = 60),
        Tag(id = 13, name = "Fashion", point = 60)
    )
}

object DummyDataSource {
    val categories = listOf(
        Category(id = 2, name = "Gaming Devices"),
        Category(id = 1, name = "Uncategorized")
    )
    val item = Item(
        id = 0,
        cardColorsId = 0,
        name = "Steam Deck",
        description = "Steam latest handheld",
        price = 8499000,
        link = "tokopedia.com"
    )
    val items = listOf(
        SimplifiedItem(
            id = 2,
            cardColorsId = 2,
            name = "Steam Deck",
            description = "Steam latest handheld",
            price = 8499000
        ),
        SimplifiedItem(
            id = 1,
            cardColorsId = 1,
            name = "Nintendo Switch",
            description = null,
            price = 3999000
        ),
        SimplifiedItem(
            id = 0,
            cardColorsId = 0,
            name = "Playstation 5",
            description = null,
            price = 7499000
        )
    )
    val doneItems = listOf(
        SimplifiedItem(
            id = 3,
            cardColorsId = 2,
            name = "Lenovo Legion Go",
            description = "Lenovo latest handheld",
            price = 8499000
        ),
        SimplifiedItem(
            id = 4,
            cardColorsId = 1,
            name = "Nintendo 3DS",
            description = null,
            price = 3999000
        ),
        SimplifiedItem(
            id = 5,
            cardColorsId = 0,
            name = "Playstation 4",
            description = null,
            price = 7499000
        )
    )
    val tags = listOf(
        Tag(id = 0, name = "Hobby", point = 25),
        Tag(id = 1, name = "Game", point = 25)
    )
}