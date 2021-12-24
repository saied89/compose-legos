package home.saied.composesamples.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import home.saied.samples.Sample

@Composable
fun SampleView(sample: Sample) {
    Text(text = sample.body)
}