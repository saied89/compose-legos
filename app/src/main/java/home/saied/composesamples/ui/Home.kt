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
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import home.saied.composesamples.R
import home.saied.composesamples.openUrl
import home.saied.composesamples.ui.search.SearchScreen
import home.saied.samples.SampleModule
import kotlin.math.abs
import kotlin.math.roundToInt

private const val GITHUB_URL = "https://github.com/saied89/compose-legos"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    moduleList: List<SampleModule>,
    onModuleClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onSearchSampleClick: (SampleWithPath) -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()
    val homeState by homeViewModel.homeState
    val searchTransition = updateTransition(homeState, "SearchTransition")
    val toolbarHeight = if (homeState is HomeViewModel.HomeState.SearchState) 56.dp else 76.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val toolBarNotScrolled = abs(toolbarOffsetHeightPx.value) < 0.1
    val statusBarInset = LocalWindowInsets.current.statusBars.layoutInsets.top
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value =
                    if (homeState == HomeViewModel.HomeState.MODULES)
                        newOffset.coerceIn(-toolbarHeightPx - statusBarInset, 0f)
                    else 0f
                return Offset.Zero
            }
        }
    }

    val systemUiController = rememberSystemUiController()
    val statusBarColor =
        if (homeState is HomeViewModel.HomeState.SearchState) MaterialTheme.colors.secondary else Color.Transparent
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
                is HomeViewModel.HomeState.SearchState -> {
                    SearchScreen(
                        it.searchResult,
                        onSearchSampleClick = onSearchSampleClick
                    )
                }
                is HomeViewModel.HomeState.MODULES -> {
                    ModuleList(
                        moduleList = moduleList,
                        onModuleClick = onModuleClick,
                        toolbarHeight = 48.dp
                    )
                }
            }
        }
        searchTransition.SearchBox(
            searchStr = homeViewModel.searchStr,
            onSearchStr = {
                homeViewModel.searchStr = it
            },
            enabled = toolBarNotScrolled,
            onLeadingClick = {
                homeViewModel.searchStr = null
            },
            onAboutClick = onAboutClick,
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Transition<HomeViewModel.HomeState>.SearchBox(
    searchStr: String?,
    enabled: Boolean,
    onLeadingClick: () -> Unit,
    onSearchStr: (String) -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val searchIsPressed by interactionSource.collectIsFocusedAsState()
    val searchFocusRequester = remember { FocusRequester() }
    var moreMenuExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = searchIsPressed) {
        if (searchIsPressed) {
            onSearchStr("")
        }
    }
    val homeState = this.currentState
    val searchCornerPercent by animateInt(label = "searchCornerDp") {
        when (it) {
            is HomeViewModel.HomeState.MODULES -> 24
            is HomeViewModel.HomeState.SearchState -> 0
        }
    }

    val seachPaddingDp by animateDp(label = "searchCornerDp") {
        when (it) {
            is HomeViewModel.HomeState.MODULES -> 12.dp
            is HomeViewModel.HomeState.SearchState -> 0.dp
        }
    }

    val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = searchStr ?: "",
            onValueChange = onSearchStr,
            enabled = enabled,
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = "Search Samples",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
            },
            leadingIcon = {
                IconButton(onClick = {
                    if (homeState is HomeViewModel.HomeState.SearchState)
                        focusManager.clearFocus()
                    else
                        searchFocusRequester.requestFocus()
                    onLeadingClick()
                }) {
                    Crossfade(animationSpec = snap()) {
                        when (it) {
                            is HomeViewModel.HomeState.SearchState -> {
                                Icon(
                                    Icons.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                            is HomeViewModel.HomeState.MODULES -> {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                }
            },
            trailingIcon = {
                Box() {
                    IconButton(onClick = { moreMenuExpanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                    }
                    val context = LocalContext.current
                    DropdownMenu(
                        expanded = moreMenuExpanded,
                        onDismissRequest = { moreMenuExpanded = false }) {
                        DropDownMenuContent(
                            onGithubClick = {
                                moreMenuExpanded = false
                                context.openUrl(GITHUB_URL)
                                            },
                            onAboutClick = {
                                moreMenuExpanded = false
                                onAboutClick()
                            }
                        )
                    }
                }
            },
            textStyle = MaterialTheme.typography.body1.copy(Color.LightGray),
            shape = RoundedCornerShape(searchCornerPercent),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = if (currentState is HomeViewModel.HomeState.SearchState) Color.Transparent else Color.Gray
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
                text = "Jetpack Compose Modules",
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(16.dp)
            )
        }
        itemsIndexed(moduleList, itemContent = { index, item ->
            ListItem(
                text = { Text(text = item.name) },
                secondaryText = {
                    // Drop .sample from package names
                    Text(text = item.packageName.dropLast(8))
                },
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

@Composable
fun ColumnScope.DropDownMenuContent(onGithubClick: () -> Unit, onAboutClick: () -> Unit) {
    DropdownMenuItem(onClick = onGithubClick) {
        Text(text = "☆ on Github")
    }
    DropdownMenuItem(onClick = onAboutClick) {
        Text(text = "About")
    }
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