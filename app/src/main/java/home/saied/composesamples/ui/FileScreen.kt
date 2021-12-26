package home.saied.composesamples.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import home.saied.samples.SampleFile

@Composable
fun FileScreen(sampleFile: SampleFile, onSampleClicked: (Int) -> Unit) {
    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        itemsIndexed(sampleFile.sampleList, itemContent = { index, item ->
            ListItem(title = item.name, Modifier.padding(horizontal = 16.dp)) {
                onSampleClicked(index)
            }
            Spacer(modifier = Modifier.height(8.dp))
        })
    }
}