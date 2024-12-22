package app.wishpedia.ui.itemdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wishpedia.data.AppRepository
import app.wishpedia.data.source.entity.Item
import app.wishpedia.data.source.entity.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ItemDetailUiState(
    val item: Item? = null,
    val tags: List<Tag>? = null,
    val isLoading: Boolean = false,
    val isItemChanged: Boolean = false,
    val markItemAsDoneDialogState: MarkItemAsDoneDialogState = MarkItemAsDoneDialogState(),
    val addEditItemDialogState: AddEditItemDialogState = AddEditItemDialogState(),
    val deleteItemDialogState: DeleteItemDialogState = DeleteItemDialogState()
)

data class MarkItemAsDoneDialogState(
    val isClosed: Boolean = true
)

data class AddEditItemDialogState(
    val isClosed: Boolean = true
)

data class DeleteItemDialogState(
    val isClosed: Boolean = true
)

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ItemDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun showMarkItemAsDoneDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                markItemAsDoneDialogState = MarkItemAsDoneDialogState(isClosed = false)
            )
        }
    }

    fun hideMarkItemAsDoneDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                markItemAsDoneDialogState = MarkItemAsDoneDialogState(isClosed = true)
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

    fun showDeleteItemDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                deleteItemDialogState = DeleteItemDialogState(isClosed = false)
            )
        }
    }

    fun hideDeleteItemDialog() {
        _uiState.update { currentState ->
            currentState.copy(
                deleteItemDialogState = DeleteItemDialogState(isClosed = true)
            )
        }
    }

    fun getItem(id: Int) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            appRepository.getItemWithTags(id).let { itemWithTags ->
                _uiState.update { currentState ->
                    currentState.copy(
                        item = itemWithTags.item,
                        tags = itemWithTags.tags,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshItem() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }
        viewModelScope.launch {
            uiState.value.item?.id?.let { id ->
                appRepository.getItem(id).let { item ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            item = item,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun updateItemPinnedState() {
        uiState.value.item?.let { item ->
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch {
                appRepository.updateItemPinnedState(item).let { updatedItem ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            item = updatedItem,
                            isLoading = false,
                            isItemChanged = true
                        )
                    }
                }
            }
        }
    }

    fun deleteItem(onSuccess: (() -> Unit)? = null) {
        uiState.value.item?.let { item ->
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch {
                appRepository.deleteItem(item)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isItemChanged = true
                    )
                }
                if (onSuccess != null) {
                    onSuccess()
                }
            }
        }
    }

    fun markItemAsDone(onSuccess: (() -> Unit)? = null) {
        uiState.value.item?.let { item ->
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            viewModelScope.launch {
                appRepository.markItemAsDone(item)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isItemChanged = true
                    )
                }
                if (onSuccess != null) {
                    onSuccess()
                }
            }
        }
    }

    fun resetUiState() {
        _uiState.update { _ ->
            ItemDetailUiState()
        }
    }
}