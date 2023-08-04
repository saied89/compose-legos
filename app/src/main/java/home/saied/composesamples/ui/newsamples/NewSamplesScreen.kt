package home.saied.composesamples.ui.newsamples

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewSamplesScreen(
    newSamples: List<NewModuleSamples>,
    navigateToSample: (sample: String, module: String) -> Unit,
    onBackPress: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "New Samples")
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            for (newModuleSample in newSamples) {
                stickyHeader {
                    ListItem(
                        headlineContent = {
                            Text(text = newModuleSample.moduleName)
                        },
                        tonalElevation = 4.dp
                    )
                }
                items(newModuleSample.samples) { sample ->
                    ListItem(
                        modifier = Modifier.clickable {
                            navigateToSample(sample, newModuleSample.moduleName)
                        },
                        headlineContent = {
                            Text(text = sample)
                        }
                    )
                }
            }
        }
    }
}
