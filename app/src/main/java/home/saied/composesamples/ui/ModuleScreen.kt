package home.saied.composesamples.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.samples.SampleModule


@ExperimentalMaterial3Api
@Composable
fun ModuleScreen(
    sampleModule: SampleModule,
    onBackClick: () -> Unit,
    onFileClick: (Int) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = sampleModule.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(sampleModule.sampleFileList,
                itemContent = { index, item ->
                    ListItem(
                        headlineContent = { Text(text = item.name) },
                        leadingContent = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_compose_file_concept),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(48.dp)
                            )
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onFileClick(index)
                            }
                    )
                }
            )
        }
    }
}