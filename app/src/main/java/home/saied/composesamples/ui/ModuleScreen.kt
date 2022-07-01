package home.saied.composesamples.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.samples.SampleModule


@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun ModuleScreen(sampleModule: SampleModule, onBackClick: () -> Unit, onFileClick: (Int) -> Unit) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    androidx.compose.material3.Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).systemBarsPadding(),
        topBar = {
            SmallTopAppBar(
                modifier = Modifier.systemBarsPadding(),
                title = {
                    Text(
                        text = sampleModule.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBackClick) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            itemsIndexed(sampleModule.sampleFileList, itemContent = { index, item ->
                ListItem(
                    text = { Text(text = item.name) },
                    icon = {
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
                Spacer(modifier = Modifier.height(8.dp))
            })
        }
    }
}