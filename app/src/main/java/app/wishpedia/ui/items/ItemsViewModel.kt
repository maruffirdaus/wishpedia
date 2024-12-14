package app.wishpedia.ui.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wishpedia.data.AppRepository
import app.wishpedia.data.source.entity.SimplifiedItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ItemsUiState(
    val featuredItems: List<SimplifiedItem> = listOf(),
    val pinnedItems: List<SimplifiedItem> = listOf(),
    val items: List<SimplifiedItem> = listOf(),
    val isLoading: Boolean = false,
    val addEditItemDialogState: AddEditItemDialogState = AddEditItemDialogState(),
    val itemDetailDialogState: ItemDetailDialogState = ItemDetailDialogState()
)

data class AddEditItemDialogState(
    val isClosed: Boolean = true,
)

data class ItemDetailDialogState(
    val itemId: Int? = null,
    val isClosed: Boolean = true,
)

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ItemsUiState())
    val uiState = _uiState.asStateFlow()

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

    fun getItems() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            appRepository.getFeaturedItems().let { items ->
                _uiState.update { currentState ->
                    currentState.copy(
                        featuredItems = items.map {
                            SimplifiedItem(it.id, it.cardColorsId, it.name, it.description, it.price)
                        }
                    )
                }
            }
            appRepository.getPinnedItems().let { items ->
                _uiState.update { currentState ->
                    currentState.copy(
                        pinnedItems = items.map {
                            SimplifiedItem(it.id, it.cardColorsId, it.name, it.description, it.price)
                        }
                    )
                }
            }
            appRepository.getItems().let { items ->
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