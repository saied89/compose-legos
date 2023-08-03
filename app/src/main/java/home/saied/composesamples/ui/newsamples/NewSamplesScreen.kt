package home.saied.composesamples.ui.newsamples

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NewSamplesScreen(newSamples: List<NewSample>) {
    Scaffold {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(newSamples) { newSample ->
                Text(text = newSample.sampleName)
            }
        }
    }
}