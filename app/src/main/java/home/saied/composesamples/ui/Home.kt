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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
import home.saied.composesamples.R
import home.saied.composesamples.openUrl
import home.saied.composesamples.ui.search.SearchScreen
import home.saied.samples.SampleModule
import kotlin.math.abs
import kotlin.math.roundToInt

private const val GITHUB_URL = "https://github.com/saied89/compose-legos"

private val sampleModuleList = List(20) {
    SampleModule("test","", emptyList())
}
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    moduleList: List<SampleModule>,
    onModuleClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onSearchSampleClick: (SampleWithPath) -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel()
//    val homeState by homeViewModel.homeState
//    val searchTransition = updateTransition(homeState, "SearchTransition")
//    val toolbarHeight = if (homeState is HomeViewModel.HomeState.SearchState) 56.dp else 52.dp
//    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
//    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
//    val toolBarNotScrolled by remember {
//        derivedStateOf {
//            abs(toolbarOffsetHeightPx.value) < 0.1
//        }
//    }
//    val statusBarInset = WindowInsets.statusBars.getTop(LocalDensity.current)
//    var newOffset by remember { mutableStateOf(0f) }
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                val delta = available.y
//                newOffset = toolbarOffsetHeightPx.value + delta
//                toolbarOffsetHeightPx.value =
//                    if (homeState == HomeViewModel.HomeState.MODULES)
//                        newOffset.coerceIn(-2 * toolbarHeightPx - statusBarInset, 0f)
//                    else 0f
//                return Offset.Zero
//            }
//        }
//    }

    var active by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun closeSearchBar() {
        focusManager.clearFocus()
        active = false
    }

    Box(Modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = "",
            onQueryChange = { },
            onSearch = { closeSearchBar() },
            active = active,
            onActiveChange = {
                active = it
                if (!active) focusManager.clearFocus()
            },
            placeholder = { Text("Search Samples") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = null
                )
            },
        ) {
            SearchScreen(
                emptyList(),
                onSearchSampleClick = onSearchSampleClick
            )
        }
        ModuleList(
            moduleList = sampleModuleList,
            onModuleClick = onModuleClick,
            toolbarHeight = 48.dp
        )
    }

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .nestedScroll(nestedScrollConnection)
//            .statusBarsPadding()
//    ) {
//        val seachPaddingDp by searchTransition.animateDp(label = "searchCornerDp") {
//            when (it) {
//                is HomeViewModel.HomeState.MODULES -> 12.dp
//                is HomeViewModel.HomeState.SearchState -> 0.dp
//            }
//        }
//        searchTransition.AnimatedContent {
//            when (it) {
//                is HomeViewModel.HomeState.SearchState -> {
//                    SearchScreen(
//                        it.searchResult,
//                        onSearchSampleClick = onSearchSampleClick
//                    )
//                }
//                is HomeViewModel.HomeState.MODULES -> {
//                    ModuleList(
//                        moduleList = moduleList,
//                        onModuleClick = onModuleClick,
//                        toolbarHeight = 48.dp
//                    )
//                }
//            }
//        }
//        searchTransition.SearchBox(
//            searchStr = homeViewModel.searchStr,
//            onSearchStr = {
//                homeViewModel.searchStr = it
//            },
//            enabled = toolBarNotScrolled,
//            onLeadingClick = {
//                homeViewModel.searchStr = null
//            },
//            onAboutClick = onAboutClick,
//            modifier = Modifier
//                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }
//                .padding(seachPaddingDp)
//                .fillMaxWidth()
//                .requiredHeight(toolbarHeight)
//        )
//    }
}


//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun Transition<HomeViewModel.HomeState>.SearchBox(
//    searchStr: String?,
//    enabled: Boolean,
//    onLeadingClick: () -> Unit,
//    onSearchStr: (String) -> Unit,
//    onAboutClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
//    val searchIsPressed by interactionSource.collectIsFocusedAsState()
//    val searchFocusRequester = remember { FocusRequester() }
//    var moreMenuExpanded by remember { mutableStateOf(false) }
//    LaunchedEffect(key1 = searchIsPressed) {
//        if (searchIsPressed) {
//            onSearchStr("")
//        }
//    }
//    val homeState = this.currentState
//    val searchCornerPercent by animateInt(label = "searchCornerDp") {
//        when (it) {
//            is HomeViewModel.HomeState.MODULES -> 24
//            is HomeViewModel.HomeState.SearchState -> 0
//        }
//    }
//
//    val focusManager = LocalFocusManager.current
//    OutlinedTextField(
//        value = searchStr ?: "",
//        onValueChange = onSearchStr,
//        enabled = enabled,
//        interactionSource = interactionSource,
//        placeholder = {
//            Text(
//                text = "Search Samples",
//                style = MaterialTheme.typography.body2.copy(color = Color.Gray)
//            )
//        },
//        leadingIcon = {
//            IconButton(onClick = {
//                if (homeState is HomeViewModel.HomeState.SearchState)
//                    focusManager.clearFocus()
//                else
//                    searchFocusRequester.requestFocus()
//                onLeadingClick()
//            }) {
//                Crossfade(animationSpec = snap()) {
//                    when (it) {
//                        is HomeViewModel.HomeState.SearchState -> {
//                            Icon(
//                                Icons.Filled.ArrowBack,
//                                contentDescription = null
//                            )
//                        }
//
//                        is HomeViewModel.HomeState.MODULES -> {
//                            Icon(
//                                Icons.Filled.Search,
//                                contentDescription = null
//                            )
//                        }
//                    }
//                }
//
//            }
//        },
//        trailingIcon = {
//            Box() {
//                IconButton(onClick = { moreMenuExpanded = true }) {
//                    Icon(Icons.Filled.MoreVert, contentDescription = null)
//                }
//                val context = LocalContext.current
//                DropdownMenu(
//                    expanded = moreMenuExpanded,
//                    onDismissRequest = { moreMenuExpanded = false }) {
//                    DropDownMenuContent(
//                        onGithubClick = {
//                            moreMenuExpanded = false
//                            context.openUrl(GITHUB_URL)
//                        }
//                    ) {
//                        moreMenuExpanded = false
//                        onAboutClick()
//                    }
//                }
//            }
//        },
//        textStyle = MaterialTheme.typography.body1.copy(Color.LightGray),
//        shape = RoundedCornerShape(searchCornerPercent),
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            backgroundColor = MaterialTheme.colors.secondary,
//            focusedBorderColor = Color.Transparent,
//            unfocusedBorderColor = if (currentState is HomeViewModel.HomeState.SearchState) Color.Transparent else Color.Gray
//        ),
//        modifier = modifier
//            .focusRequester(searchFocusRequester)
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleList(toolbarHeight: Dp, moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(top = toolbarHeight + 16.dp)) {
        item {
            Text(
                text = "Jetpack Compose Modules",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        itemsIndexed(moduleList, itemContent = { index, item ->
            ListItem(
                headlineContent = { Text(text = item.name) },
                supportingContent = {
                    // Drop .sample from package names
                    Text(text = item.cleanPackageName)
                },
                leadingContent = {
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
fun DropDownMenuContent(onGithubClick: () -> Unit, onAboutClick: () -> Unit) {
    DropdownMenuItem(
        onClick = onGithubClick,
        text = {
            Text(text = "☆ on Github")
        }
    )
    DropdownMenuItem(
        onClick = onAboutClick,
        text = {
            Text(text = "About")
        }
    )
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