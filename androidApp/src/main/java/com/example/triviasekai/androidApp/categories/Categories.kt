package com.example.triviasekai.androidApp.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviasekai.androidApp.R
import com.example.triviasekai.androidApp.TriviaViewModel
import com.example.triviasekai.androidApp.ui.TriviaColors
import com.example.triviasekai.androidApp.ui.TriviaIcon
import com.example.triviasekai.androidApp.ui.TriviaIcons

@Composable
fun CategoriesScreen(viewModel: TriviaViewModel, onLevelSelected: (Int, Int, String) -> Unit) {
    val categoriesState = viewModel.categoriesSharedFlow().collectAsState(initial = null)
    CategoriesContent(list = categoriesState.value) { level, categoryId, title ->
        onLevelSelected(
            level,
            categoryId,
            title
        )
    }
}

@Composable
fun CategoriesContent(list: List<CategoryViewItem>?, onLevelSelected: (Int, Int, String) -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val selectedCategory: MutableState<CategoryViewItem?> = remember { mutableStateOf(null) }
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Select a category") }) }
    ) {
        list?.let {
            Column {
                CategoriesList(list = list) {
                    showDialog.value = true
                    selectedCategory.value = it
                }
                LevelDialog(showDialog.value, { showDialog.value = false }) { level ->
                    onLevelSelected(
                        level,
                        selectedCategory.value?.id ?: error("No Category selected"),
                        selectedCategory.value?.name ?: error("No Category selected")
                    )
                }
            }
        } ?: run {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}

@Composable
fun CategoriesList(list: List<CategoryViewItem>, onItemClick: (CategoryViewItem) -> Unit) {
    LazyColumn(
        Modifier.fillMaxHeight(1f),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(list) {
            CategoryItem(
                category = it,
                modifier = Modifier.clickable(onClick = { onItemClick(it) })
            )
        }
    }
}

@Composable
fun CategoryItem(category: CategoryViewItem, modifier: Modifier) {
    Column {
        Spacer(modifier = Modifier.size(4.dp))
        Card(modifier.fillMaxWidth(), backgroundColor = Color(category.color.color)) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                TriviaIcon(
                    resourceId = (TriviaIcons.values()
                        .find { it.id == category.id })?.resourceId
                        ?: TriviaIcons.Knowledge.resourceId,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = category.name,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold_italic))
                )
            }
        }
        Spacer(modifier = Modifier.size(4.dp))
    }
}

@Preview
@Composable
fun CategoryItemPreview() {
    CategoryItem(
        CategoryViewItem(0, "title", TriviaIcons.Knowledge, TriviaColors.White),
        Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun ScreenPreview() {
    CategoriesContent(
        list = listOf(
            CategoryViewItem(0, "Entertainment", TriviaIcons.Knowledge, TriviaColors.White),
            CategoryViewItem(0, "Sports", TriviaIcons.Knowledge, TriviaColors.White),
            CategoryViewItem(0, "Maths", TriviaIcons.Knowledge, TriviaColors.White),
            CategoryViewItem(
                0,
                "General Knowledge or something even longer to break the layout",
                TriviaIcons.Knowledge,
                TriviaColors.White
            )
        )
    ) { _, _, _ -> }
}