package app.wishpedia.addeditcategory

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

data class AddEditCategoryUiState(
    val name: String = "",
    val isSaveButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditCategoryUiState())
    val uiState = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                isSaveButtonEnabled = name.isNotEmpty()
            )
        }
    }

    fun saveCategory() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true)
        }

        val category = Category(id = 0, name = uiState.value.name)
        viewModelScope.launch {
            appRepository.addCategories(category)
        }

        _uiState.update { currentState ->
            currentState.copy(isLoading = false)
        }
    }

    fun resetUiState() {
        _uiState.update { _ ->
            AddEditCategoryUiState()
        }
    }
}