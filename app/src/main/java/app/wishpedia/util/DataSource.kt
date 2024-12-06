package app.wishpedia.util

import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.SimplifiedItem
import app.wishpedia.data.source.entity.Tag

object InitialDataSource {
    val category = Category(1, "Uncategorized")
    val tags = listOf(
        Tag(0, "Other", 0),
        Tag(1, "Work", 50),
        Tag(2, "School", 50),
        Tag(3, "Sport", 35),
        Tag(4, "Hobby", 25),
        Tag(5, "Game", 25)
    )
}

object DummyDataSource {
    val categories = listOf(
        Category(2, "Gaming Devices"),
        Category(1, "Uncategorized")
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
        Tag(0, "Hobby", 25),
        Tag(1, "Game", 25)
    )
}