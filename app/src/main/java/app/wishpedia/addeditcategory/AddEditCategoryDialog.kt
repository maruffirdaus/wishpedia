package app.wishpedia.addeditcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import app.wishpedia.ui.theme.WishpediaTheme

@Composable
fun AddEditCategoryDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    categoryId: Int? = null,
    viewModel: AddEditCategoryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        categoryId?.let {

        }
    }
    Dialog(onDismissRequest = onDismissRequest) {
        val uiState by viewModel.uiState.collectAsState()
        AddEditCategoryContent(
            name = uiState.name,
            saveButtonEnabled = uiState.isSaveButtonEnabled,
            onNameChange = viewModel::updateName,
            onDismissRequest = {
                viewModel.resetUiState()
                onDismissRequest()
            },
            onConfirmRequest = {
                viewModel.saveCategory()
                onConfirmRequest()
            }
        )
    }
}

@Composable
fun AddEditCategoryContent(
    name: String,
    saveButtonEnabled: Boolean,
    onNameChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Add category",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                name,
                onValueChange = {
                    onNameChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Name")
                },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                TextButton(
                    onClick = {
                        onConfirmRequest()
                        onDismissRequest()
                    },
                    enabled = saveButtonEnabled
                ) {
                    Text("Save")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun AddEditCategoryContentPreview() {
    WishpediaTheme(dynamicColor = false) {
        AddEditCategoryContent(
            name = "",
            saveButtonEnabled = false,
            onNameChange = {},
            onDismissRequest = {},
            onConfirmRequest = {}
        )
    }
}