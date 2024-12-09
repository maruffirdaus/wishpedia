package app.wishpedia.categorizeditems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wishpedia.data.AppRepository
import app.wishpedia.data.source.entity.Category
import app.wishpedia.data.source.entity.SimplifiedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategorizedItemsUiState(
    val category: Category? = null,
    val pinnedItems: List<SimplifiedItem> = listOf(),
    val items: List<SimplifiedItem> = listOf(),
    val isLoading: Boolean = false,
    val addEditCategoryDialogState: AddEditCategoryDialogState = AddEditCategoryDialogState(),
    val deleteCategoryDialogState: DeleteCategoryDialogState = DeleteCategoryDialogState(),
    val addEditItemDialogState: AddEditItemDialogState = AddEditItemDialogState(),
    val itemDetailDialogState: ItemDetailDialogState = ItemDetailDialogState()
)

data class AddEditCategoryDialogState(
    val isClosed: Boolean = true
)

data class DeleteCategoryDialogState(
    val isClosed: Boolean = true
)

data class AddEditItemDialogState(
    val isClosed: Boolean = true
)

data class ItemDetailDialogState(
    val itemId: Int? = null,
    val isClosed: Boolean = true
)

@HiltViewModel
class CategorizedItemsViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategorizedItemsUiState())
    val uiState = _uiState.asStateFlow()

    fun showAddEditCategoryDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                addEditCategoryDialogState = AddEditCategoryDialogState(isClosed = false)
            )
        }
    }

    fun hideAddEditCategoryDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                addEditCategoryDialogState = AddEditCategoryDialogState(isClosed = true)
            )
        }
    }

    fun showDeleteCategoryDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                deleteCategoryDialogState = DeleteCategoryDialogState(isClosed = false)
            )
        }
    }

    fun hideDeleteCategoryDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                deleteCategoryDialogState = DeleteCategoryDialogState(isClosed = true)
            )
        }
    }

    fun showAddEditItemDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                addEditItemDialogState = AddEditItemDialogState(isClosed = false)
            )
        }
    }

    fun hideAddEditItemDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                addEditItemDialogState = AddEditItemDialogState(isClosed = true)
            )
        }
    }

    fun showItemDetailPopup(itemId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                itemDetailDialogState = ItemDetailDialogState(
                    itemId = itemId,
                    isClosed = false
                )
            )
        }
    }

    fun hideItemDetailPopup() {
        _uiState.update { currentState ->
            currentState.copy(
                itemDetailDialogState = ItemDetailDialogState(isClosed = true)
            )
        }
    }

    fun initScreen(categoryId: Int) {
        getCategory(categoryId)
        getItems()
    }

    fun getCategory(categoryId: Int) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            appRepository.getCategory(categoryId).let { category ->
                _uiState.update { currentState ->
                    currentState.copy(
                        category = category,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getItems() {
        uiState.value.category?.let { category ->
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch {
                appRepository.getPinnedItems(category.id).let { items ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            pinnedItems = items.map {
                                SimplifiedItem(it.id, it.cardColorsId, it.name, it.description, it.price)
                            }
                        )
                    }
                }
                appRepository.getItems(category.id).let { items ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            items = items.map {
                                SimplifiedItem(it.id, it.cardColorsId, it.name, it.description, it.price)
                            },
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun deleteCategory(onSuccess: (() -> Unit)? = null){
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch{
            uiState.value.category?.let { category ->
                appRepository.deleteCategory(category)
            }
            onSuccess?.let { onSuccess ->
                onSuccess()
            }
        }
    }
}