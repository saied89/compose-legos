package home.saied.composesamples.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.samples.SampleFile
import home.saied.samples.SampleModule

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModuleScreen(sampleModule: SampleModule, onFileClick: (Int) -> Unit) {
    LazyColumn {
        item {
            val titleAnnotatedString = with(AnnotatedString.Builder()) {

                pushStyle(SpanStyle(fontWeight = FontWeight.Medium, fontStyle = FontStyle.Italic))
                append(sampleModule.name)
                pop()
                append(" sample files")
                toAnnotatedString()
            }
            Text(
                text = titleAnnotatedString,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(16.dp)
            )
        }
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

@Preview
@Composable
fun Preview() {
    ModuleScreen(sampleModule = SampleModule("animation-core", "", listOf(SampleFile("First File", listOf()))), onFileClick = {})
}