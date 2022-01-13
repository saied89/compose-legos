package home.saied.composesamples.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.samples.Sample
import home.saied.samples.sampleModules

//data class SampleWithPath(
//    val sample: Sample,
//    val sampleModuleIndex: Int,
//    val sampleFileIndex: Int
//)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchScreen(searchStr: String) {
    val searchRes = run {
        data class SampleWithPath(
            val sample: Sample,
            val moduleIndex: Int,
            val fileIndex: Int,
            val sampleIndex: Int
        )
        sampleModules.asSequence()
            .flatMapIndexed { moduleIndex, module ->
                module.sampleFileList.asSequence().flatMapIndexed { fileIndex, file ->
                    file.sampleList.asSequence().mapIndexed { sampleIndex, sample ->
                        SampleWithPath(sample, moduleIndex, fileIndex, sampleIndex)
                    }
                }
            }
            .filter {
                it.sample.name.contains(searchStr)
            }.toList()
    }
    LazyColumn {
        items(searchRes) { item ->
            ListItem(
                text = { Text(text = item.sample.name) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_compose_sample_concept),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(48.dp)
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {

                    }
            )
        }
    }
}