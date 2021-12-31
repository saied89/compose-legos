package home.saied.composesamples.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import home.saied.samples.SampleModule
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {

    val searchBoxInteractionSource = remember { MutableInteractionSource() }
    val searchIsPressed by searchBoxInteractionSource.collectIsFocusedAsState()
    val homeState: HomeState by derivedStateOf {
        if (searchIsPressed)
            HomeState.SEARCH
        else HomeState.MODULES

    }
    val searchTransition = updateTransition(homeState, "SearchTransition")
    val toolbarHeight = if (homeState == HomeState.SEARCH) 56.dp else 84.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value =
                    if (homeState == HomeState.MODULES)
                        newOffset.coerceIn(-toolbarHeightPx, 0f)
                    else 0f
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        searchTransition.AnimatedContent {
            when (it) {
                HomeState.SEARCH -> {
                    ModuleList(moduleList = moduleList, onModuleClick = onModuleClick, toolbarHeight = 56.dp)
                }
                HomeState.MODULES -> {
                    ModuleList(moduleList = moduleList, onModuleClick = onModuleClick, toolbarHeight = 48.dp)
                }
            }
        }
        searchTransition.SearchBox(
            interactionSource = searchBoxInteractionSource,
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Transition<HomeState>.SearchBox(
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier
) {
    val homeState = this.currentState
    val searchFocusRequester = remember { FocusRequester() }
    val searchCornerDp by animateDp(label = "searchCornerDp") {
        when (it) {
            HomeState.MODULES -> 24.dp
            HomeState.SEARCH -> 0.dp
        }
    }

    val seachPaddingDp by animateDp(label = "searchCornerDp") {
        when (it) {
            HomeState.MODULES -> 16.dp
            HomeState.SEARCH -> 0.dp
        }
    }

    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = "",
        onValueChange = {},
        interactionSource = interactionSource,
        placeholder = { Text(text = "Search Samples") },
        leadingIcon = {
            IconButton(onClick = {
                if (homeState == HomeState.SEARCH)
                    focusManager.clearFocus()
                else
                    searchFocusRequester.requestFocus()
            }) {
                Crossfade(animationSpec = snap()) {
                    when (it) {
                        HomeState.SEARCH -> {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                        HomeState.MODULES -> {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                    }
                }

            }
        },
        textStyle = MaterialTheme.typography.titleSmall,
        shape = RoundedCornerShape(searchCornerDp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.LightGray,
            focusedBorderColor = Color.Transparent
        ),
        modifier = modifier
            .padding(seachPaddingDp)
            .fillMaxWidth()
            .focusRequester(searchFocusRequester)
    )
}

@Composable
fun ModuleList(toolbarHeight: Dp, moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(top = toolbarHeight + 16.dp)) {
        item { 
            Spacer(modifier = Modifier.height(16.dp))
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