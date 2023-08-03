package home.saied.composesamples.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import home.saied.composesamples.R
import home.saied.composesamples.openUrl
import home.saied.composesamples.ui.search.SearchScreen
import home.saied.samples.SampleModule
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val GITHUB_URL = "https://github.com/saied89/compose-legos"

private val SearchbarHeight = 64.dp

@Composable
fun HomeScreen(
    moduleList: List<SampleModule>,
    onModuleClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onSearchSampleClick: (SampleWithPath) -> Unit
) {
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.factory(moduleList)
    )
    val searchbarHeightPx = with(LocalDensity.current) { SearchbarHeight.roundToPx().toFloat() }
    val searchbarOffsetHeightPx = remember { mutableStateOf(0f) }
    var newOffset by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                newOffset = searchbarOffsetHeightPx.value + delta
                searchbarOffsetHeightPx.value =
                    newOffset.coerceIn(-2 * searchbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val state by homeViewModel.homeState.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(50.dp))
                NavigationDrawerItem(
                    label = { Text(text = "New Samples") },
                    icon = {
                        Icon(imageVector = Icons.Default.NewReleases, contentDescription = null)
                    },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
            }
        }
    ) {
        Scaffold {
            Box(
                Modifier.nestedScroll(nestedScrollConnection)
            ) {
                var searchActive by rememberSaveable { mutableStateOf(false) }
                val scope = rememberCoroutineScope()
                SearchBar(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset {
                            IntOffset(
                                x = 0,
                                y = if (searchActive) 0 else searchbarOffsetHeightPx.value.roundToInt()
                            )
                        },
                    searchStr = state.searchStr,
                    setSearchStr = homeViewModel::setSearchStr,
                    searchActive = searchActive,
                    setSearchActive = { searchActive = it },
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onAboutClick = onAboutClick
                ) {
                    LaunchedEffect(key1 = searchActive) {
                        if (!searchActive)
                            searchbarOffsetHeightPx.value = 0f
                    }
                    SearchScreen(
                        searchRes = state.searchResult,
                        onSearchSampleClick = onSearchSampleClick
                    )
                }
                ModuleList(
                    moduleList = moduleList,
                    onModuleClick = onModuleClick,
                    toolbarHeight = 48.dp,
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    modifier: Modifier,
    searchActive: Boolean,
    setSearchActive: (Boolean) -> Unit,
    searchStr: String,
    setSearchStr: (String) -> Unit,
    onAboutClick: () -> Unit,
    onMenuClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SearchBar(
        modifier = modifier,
        query = searchStr,
        onQueryChange = setSearchStr,
        onSearch = { },
        active = searchActive,
        onActiveChange = setSearchActive,
        placeholder = { Text("Search Samples") },
        leadingIcon = {
            if (searchActive)
                IconButton(
                    onClick = {
                        setSearchActive(false)
                        setSearchStr("")
                    },
                    content = {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                )
            else
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }
        },
        trailingIcon = {
            Box {
                var moreMenuExpanded by remember { mutableStateOf(false) }
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
                        }
                    ) {
                        moreMenuExpanded = false
                        onAboutClick()
                    }
                }
            }
        },
        content = content
    )
}

@Composable
fun ModuleList(
    toolbarHeight: Dp,
    moduleList: List<SampleModule>,
    onModuleClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(top = toolbarHeight, bottom = 56.dp)
    ) {
        item {
            Text(
                text = "Jetpack Compose Modules",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 4.dp)
            )
        }
        itemsIndexed(moduleList) { index, item ->
            ListItem(
                headlineContent = { Text(text = item.name) },
                supportingContent = {
                    // Drop .sample from package names
                    Text(
                        text = item.cleanPackageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
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
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
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