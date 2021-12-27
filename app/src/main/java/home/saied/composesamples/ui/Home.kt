package home.saied.composesamples.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import home.saied.samples.SampleModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(moduleList: List<SampleModule>, onModuleClick: (Int) -> Unit) {
    LazyColumn {
        item {
//            Text(text = moduleList[1].sampleFileList[0].name)
            Spacer(modifier = Modifier.height(32.dp))
        }
        itemsIndexed(moduleList, itemContent = { index, item ->
            ListItem(title = item.name, Modifier.padding(horizontal = 16.dp)) {
                onModuleClick(index)
            }
            Spacer(modifier = Modifier.height(8.dp))
        })
    }
}