package app.wishpedia.addedititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wishpedia.data.AppRepository
import app.wishpedia.data.source.entity.Item
import app.wishpedia.util.InitialDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditItemUiState(
    val cardColorsId: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String? = null,
    val price: String = "",
    val link: String = "",
    val selectedTags: List<Boolean> = List(InitialDataSource.tags.size) { false },
    val isSaveButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class AddEditItemViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private var itemId = 0
    private var categoryId = 1
    private var isItemPinned = false
    private val _uiState = MutableStateFlow(AddEditItemUiState())
    val uiState = _uiState.asStateFlow()

    fun updateCategoryId(categoryId: Int) {
        this.categoryId = categoryId
    }

    fun updateCardColorsId(cardColorsId: Int) {
        _uiState.update { currentState ->
            currentState.copy(cardColorsId = cardColorsId)
        }
    }

    fun updateName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                isSaveButtonEnabled = name.isNotEmpty() && uiState.value.selectedTags.contains(true)
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { currentState ->
            currentState.copy(description = description)
        }
    }

    fun updateImage(image: String?) {
        _uiState.update { currentState ->
            currentState.copy(image = image)
        }
    }

    fun updatePrice(price: String) {
        _uiState.update { currentState ->
            currentState.copy(price = price)
        }
    }

    fun updateLink(link: String) {
        _uiState.update { currentState ->
            currentState.copy(link = link)
        }
    }

    fun updateSelectedTags(index: Int) {
        _uiState.update { currentState ->
            val selectedTags = currentState.selectedTags.toMutableList()
            selectedTags[index] = !selectedTags[index]
            currentState.copy(
                selectedTags = selectedTags,
                isSaveButtonEnabled = uiState.value.name.isNotEmpty() && selectedTags.contains(true)
            )
        }
    }

    suspend fun saveItem() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        val item = Item(
            id = itemId,
            categoryId = categoryId,
            cardColorsId = uiState.value.cardColorsId,
            name = uiState.value.name,
            description = uiState.value.description.let {
                it.ifEmpty {
                    null
                }
            },
            image = uiState.value.image,
            price = uiState.value.price.let {
                if (it.isNotEmpty()) {
                    it.toInt()
                } else {
                    null
                }
            },
            link = uiState.value.link.let {
                it.ifEmpty {
                    null
                }
            },
            isPinned = isItemPinned
        )
        val tagIds: MutableList<Int> = mutableListOf()
        uiState.value.selectedTags.forEachIndexed { index, value ->
            if (value) tagIds += index
        }
        if (itemId == 0) {
            appRepository.addItem(item, tagIds)
        } else {
            appRepository.updateItem(item, tagIds)
        }
        _uiState.update { currentState ->
            currentState.copy(isLoading = false)
        }
    }

    fun getItem(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            appRepository.getItemWithTags(id).let { itemWithTags ->
                val item = itemWithTags.item
                itemId = item.id
                categoryId = item.categoryId
                isItemPinned = item.isPinned
                _uiState.update { currentState ->
                    val tags = itemWithTags.tags
                    val selectedTags = currentState.selectedTags.toMutableList()
                    tags.forEach { tag ->
                        selectedTags[tag.id] = true
                    }
                    currentState.copy(
                        cardColorsId = item.cardColorsId,
                        name = item.name,
                        description = item.description ?: "",
                        image = item.image,
                        price = item.price?.toString() ?: "",
                        link = item.link ?: "",
                        selectedTags = selectedTags,
                        isSaveButtonEnabled = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun resetUiState() {
        itemId = 0
        categoryId = 1
        isItemPinned = false
        _uiState.update { _ ->
            AddEditItemUiState()
        }
    }
}