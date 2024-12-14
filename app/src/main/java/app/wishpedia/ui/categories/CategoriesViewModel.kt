package app.wishpedia.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wishpedia.data.AppRepository
import app.wishpedia.data.source.entity.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesUiState(
    val categories: List<Category> = listOf(),
    val addEditCategoryDialogState: AddEditCategoryDialogState = AddEditCategoryDialogState()
)

data class AddEditCategoryDialogState(
    val isClosed: Boolean = true
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesUiState())
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

    fun getCategories() {
        viewModelScope.launch {
            appRepository.getCategories().let { categories ->
                _uiState.update { currentState ->
                    currentState.copy(categories = categories)
                }
            }
        }
    }
}