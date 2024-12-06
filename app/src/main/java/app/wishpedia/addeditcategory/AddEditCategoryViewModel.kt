package app.wishpedia.addeditcategory

import androidx.lifecycle.ViewModel
import app.wishpedia.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AddEditCategoryUiState(
    val name: String = "",
    val isSaveButtonEnabled: Boolean = false
)

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    appRepository: AppRepository
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

    fun resetUiState() {
        _uiState.update { _ ->
            AddEditCategoryUiState()
        }
    }
}