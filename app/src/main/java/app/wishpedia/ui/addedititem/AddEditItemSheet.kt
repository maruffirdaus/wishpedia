package app.wishpedia.ui.addedititem

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.wishpedia.R
import app.wishpedia.ui.theme.ItemCardColors
import app.wishpedia.ui.theme.WishpediaTheme
import app.wishpedia.utils.InitialDataSource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemSheet(
    onDismissRequest: (Boolean) -> Unit,
    itemId: Int? = null,
    categoryId: Int? = null,
    viewModel: AddEditItemViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        itemId?.let { id ->
            viewModel.getItem(id)
        }
        categoryId?.let { categoryId ->
            viewModel.updateCategoryId(categoryId)
        }
    }
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = {
            onDismissRequest(false)
            viewModel.resetUiState()
        }
    ) {
        val uiState by viewModel.uiState.collectAsState()
        AddEditItemContent(
            cardColorsId = uiState.cardColorsId,
            name = uiState.name,
            description = uiState.description,
            image = uiState.image,
            price = uiState.price,
            link = uiState.link,
            selectedTags = uiState.selectedTags,
            saveButtonEnabled = uiState.isSaveButtonEnabled,
            loading = uiState.isLoading,
            onCardColorsIdChange = viewModel::updateCardColorsId,
            onNameChange = viewModel::updateName,
            onDescriptionChange = viewModel::updateDescription,
            onImageChange = viewModel::updateImage,
            onPriceChange = viewModel::updatePrice,
            onLinkChange = viewModel::updateLink,
            onSelectedTagsChange = viewModel::updateSelectedTags,
            onSaveButtonClick = {
                viewModel.saveItem(
                    onSuccess = {
                        onDismissRequest(true)
                        viewModel.resetUiState()
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddEditItemContent(
    cardColorsId: Int,
    name: String,
    description: String,
    image: String?,
    price: String,
    link: String,
    selectedTags: List<Boolean>,
    saveButtonEnabled: Boolean,
    loading: Boolean,
    onCardColorsIdChange: (Int) -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageChange: (String?) -> Unit,
    onPriceChange: (String) -> Unit,
    onLinkChange: (String) -> Unit,
    onSelectedTagsChange: (Int) -> Unit,
    onSaveButtonClick: () -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        val tags = remember { InitialDataSource.tags }
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            item {
                Spacer(modifier = Modifier.width(24.dp))
            }
            items(ItemCardColors.availableColors, key = { colors -> colors.id }) { colors ->
                ColorChip(
                    colors = colors,
                    selected = colors.id == cardColorsId,
                    onClick = {
                        onCardColorsIdChange(colors.id)
                    },
                    enabled = !loading
                )
            }
            item {
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Select an item card color",
            modifier = Modifier.padding(start = 40.dp, end = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (image != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                GlideImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                FilledTonalIconButton(
                    onClick = { onImageChange(null) },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomEnd),
                    enabled = !loading
                ) {
                    Icon(painterResource(R.drawable.ic_delete), null)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            val imagePickerLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                result.data?.data?.let { image ->
                    onImageChange(image.toString())
                }
            }
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            TextButton(
                onClick = {
                    imagePickerLauncher.launch(intent)
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = !loading
            ) {
                Icon(painterResource(R.drawable.ic_add_photo_alternate), null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add image")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        OutlinedTextField(
            name,
            onValueChange = { onNameChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            enabled = !loading,
            label = {
                Text("Name")
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            description,
            onValueChange = { onDescriptionChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            enabled = !loading,
            label = {
                Text("Description")
            },
            supportingText = {
                Text("Optional")
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            OutlinedTextField(
                price,
                onValueChange = { onPriceChange(it) },
                modifier = Modifier.weight(1.0f),
                enabled = !loading,
                label = {
                    Text("Price")
                },
                prefix = {
                    Text("Rp")
                },
                supportingText = {
                    Text("Optional")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                link,
                onValueChange = { onLinkChange(it) },
                modifier = Modifier.weight(1.0f),
                enabled = !loading,
                label = {
                    Text("Website URL")
                },
                supportingText = {
                    Text("Optional")
                },
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
            itemsIndexed(tags, key = { _, tag -> tag.id }) { index, tag ->
                FilterChip(
                    selected = selectedTags[index],
                    onClick = { onSelectedTagsChange(index) },
                    label = {
                        Text(tag.name)
                    },
                    enabled = !loading
                )
            }
            item {
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Select one or more tags",
            modifier = Modifier.padding(start = 40.dp, end = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onSaveButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            enabled = saveButtonEnabled && !loading
        ) {
            Icon(painterResource(R.drawable.ic_save), null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun AddEditItemContentPreview() {
    WishpediaTheme(dynamicColor = false) {
        Surface {
            AddEditItemContent(
                cardColorsId = 0,
                name = "",
                description = "",
                image = null,
                price = "",
                link = "",
                selectedTags = List(InitialDataSource.tags.size) { false },
                saveButtonEnabled = false,
                loading = false,
                onCardColorsIdChange = {},
                onNameChange = {},
                onDescriptionChange = {},
                onImageChange = {},
                onPriceChange = {},
                onLinkChange = {},
                onSelectedTagsChange = {},
                onSaveButtonClick = {}
            )
        }
    }
}