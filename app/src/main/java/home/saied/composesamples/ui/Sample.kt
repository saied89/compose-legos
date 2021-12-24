package home.saied.composesamples.ui

import androidx.compose.animation.Crossfade
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import home.saied.samples.Sample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleView(sample: Sample) {
    var sampleViewSwitchState: SampleViewSwitch by remember {
        mutableStateOf(SampleViewSwitch.SOURCE)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                sampleViewSwitchState = when (sampleViewSwitchState) {
                    SampleViewSwitch.SOURCE -> {
                        SampleViewSwitch.COMPOSABLE
                    }
                    SampleViewSwitch.COMPOSABLE -> {
                        SampleViewSwitch.SOURCE
                    }
                }
            }) {
                Icon(
                    imageVector = when (sampleViewSwitchState) {
                        SampleViewSwitch.SOURCE -> {
                            Icons.Default.PlayArrow
                        }
                        SampleViewSwitch.COMPOSABLE -> {
                            Icons.Default.Code
                        }
                    },
                    contentDescription = null
                )
            }
        }
    ) {
        Crossfade(targetState = sampleViewSwitchState) {
            when (it) {
                SampleViewSwitch.SOURCE -> {
                    Text(text = sample.body)
                }
                SampleViewSwitch.COMPOSABLE -> {
                    sample.block()
                }
            }
        }

    }
}

enum class SampleViewSwitch {
    SOURCE, COMPOSABLE
}