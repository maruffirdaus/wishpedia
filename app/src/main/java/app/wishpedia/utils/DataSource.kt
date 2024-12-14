package app.wishpedia.utils

import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.data.source.entity.Tag

object InitialDataSource {
    val category = Category(1, "Uncategorized")
    val tags = listOf(
        Tag(id = 0, name = "Other", point = 0),
        Tag(id = 1, name = "Work", point = 50),
        Tag(id = 2, name = "School", point = 50),
        Tag(id = 3, name = "Sport", point = 35),
        Tag(id = 4, name = "Hobby", point = 25),
        Tag(id = 5, name = "Game", point = 25)
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
    val tags = listOf(
        Tag(id = 0, name = "Hobby", point = 25),
        Tag(id = 1, name = "Game", point = 25)
    )
}