package home.saied.composesamples.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import home.saied.samples.Sample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(sampleList: List<Sample>, onSampleClick: (Int) -> Unit) {
    Scaffold { paddingValues ->
        LazyColumn {
            item { 
                Spacer(modifier = Modifier.height(32.dp))
            }
            itemsIndexed(sampleList, itemContent = { index, item ->
                SampleItem(example = item, Modifier.padding(horizontal = 16.dp)) {
                    onSampleClick(index)
                }
                Spacer(modifier = Modifier.height(8.dp))
            })
        }
    }
}