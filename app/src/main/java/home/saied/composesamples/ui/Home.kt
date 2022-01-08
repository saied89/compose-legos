package home.saied.composesamples.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import home.saied.composesamples.R
import home.saied.samples.SampleModule
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
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
    val toolbarHeight = if (homeState == HomeState.SEARCH) 56.dp else 76.dp
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

    val systemUiController = rememberSystemUiController()
    val statusBarColor =
        if (homeState == HomeState.SEARCH) MaterialTheme.colors.secondary else Color.White
    SideEffect {
        systemUiController.setStatusBarColor(statusBarColor, darkIcons = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .systemBarsPadding()
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
    val searchCornerPercent by animateInt(label = "searchCornerDp") {
        when (it) {
            HomeState.MODULES -> 25
            HomeState.SEARCH -> 0
        }
    }

    val seachPaddingDp by animateDp(label = "searchCornerDp") {
        when (it) {
            HomeState.MODULES -> 12.dp
            HomeState.SEARCH -> 0.dp
        }
    }

    val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = "",
            onValueChange = {},
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = "Search Samples",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
            },
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
            textStyle = MaterialTheme.typography.body1.copy(Color.LightGray),
            shape = RoundedCornerShape(searchCornerPercent),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Gray
            ),
            modifier = modifier
                .padding(seachPaddingDp)
                .fillMaxWidth()
                .focusRequester(searchFocusRequester)
        )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModuleList(toolbarHeight: Dp, moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(top = toolbarHeight + 16.dp)) {
        item {
            Text(
                text = "List of Jetpack Compose Modules",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(16.dp)
            )
        }
        itemsIndexed(moduleList, itemContent = { index, item ->
            ListItem(
                text = { Text(text = item.name) },
                secondaryText = { Text(text = item.packageName) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_compose_module_2),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                },
                modifier = Modifier.clickable {
                    onModuleClick(index)
                })
            Spacer(modifier = Modifier.height(8.dp))
        })
    }
}

enum class HomeState {
    MODULES, SEARCH
}

@Preview
@Composable
fun Prev() {
    Icon(
        painter = painterResource(id = R.drawable.ic_compose_module_2),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = Modifier.size(56.dp)
    )
}