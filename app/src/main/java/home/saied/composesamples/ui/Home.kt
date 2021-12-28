package home.saied.composesamples.ui

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import home.saied.samples.SampleModule

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {

    val searchBoxInteractionSource = remember { MutableInteractionSource() }
    val searchIsPressed by searchBoxInteractionSource.collectIsFocusedAsState()
    val homeState =
        if (searchIsPressed)
            HomeState.SEARCH
        else HomeState.MODULES


    val searchTransition = updateTransition(homeState, "SearchTransition")

    val searchCornerDp by searchTransition.animateDp(label = "searchCornerDp") {
        when (it) {
            HomeState.MODULES -> 12.dp
            HomeState.SEARCH -> 0.dp
        }
    }

    val seachPaddingDp by searchTransition.animateDp(label = "searchCornerDp") {
        when (it) {
            HomeState.MODULES -> 16.dp
            HomeState.SEARCH -> 0.dp
        }
    }

    val focusManager = LocalFocusManager.current
    Column {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            interactionSource = searchBoxInteractionSource,
            placeholder = { Text(text = "Search Samples") },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        focusManager.clearFocus()
                    })
            },
            textStyle = MaterialTheme.typography.titleSmall,
            shape = RoundedCornerShape(searchCornerDp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.LightGray,
                focusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .padding(seachPaddingDp)
                .fillMaxWidth()
        )
        ModuleList(moduleList = moduleList, onModuleClick = onModuleClick)
    }

}


@Composable
fun SearchBox() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        TextField(
            value = "",
            onValueChange = {},
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            textStyle = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ModuleList(moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {
    LazyColumn {
        item {
//            Text(text = moduleList[1].sampleFileList[0].name)
            Spacer(modifier = Modifier.height(32.dp))
        }
        itemsIndexed(moduleList, itemContent = { index, item ->
            ListItem(title = item.name, Modifier.padding(horizontal = 16.dp)) {
                onModuleClick(index)
            }
            Spacer(modifier = Modifier.height(8.dp))
        })
    }
}

enum class HomeState {
    MODULES, SEARCH
}