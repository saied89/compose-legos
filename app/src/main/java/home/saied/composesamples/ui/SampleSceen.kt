package home.saied.composesamples.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import home.saied.samples.Sample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleScreen(sample: Sample) {
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
                    Code(code = sample.body)
                }
                SampleViewSwitch.COMPOSABLE -> {
                    sample.block?.invoke()
                }
            }
        }

    }
}

@Composable
private fun Code(code: String) {
    val lines = remember {
        code.lines()
    }
    val gutterWidth = lines.size.toString().length
    Column {
        lines.forEachIndexed { index, line ->
            CodeLine(index = index, codeLine = line, gutterWidth = gutterWidth)
        }
    }
}

@Composable
private fun CodeLine(index: Int, codeLine: String, gutterWidth: Int) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Text(
            text = index.toString().padStart(gutterWidth, '0'),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(2.dp)
                .align(CenterVertically)
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.DarkGray)
        )
        Text(
            text = codeLine,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.align(CenterVertically)
        )
    }
}

@Preview
@Composable
fun CodeLinePreview() {
    CodeLine(index = 0, codeLine = "test", 1)
}


//@Composable
//@Preview
//fun Preview() {
//    Code(code = """
//    |fun BasicTextFieldSample() {
//    |    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
//    |        mutableStateOf(TextFieldValue())
//    |    }
//    |    BasicTextField(
//    |        value = value,
//    |        onValueChange = {
//    |            // it is crucial that the update is fed back into BasicTextField in order to
//    |            // see updates on the text
//    |            value = it
//    |        }
//    |    )
//    |}
//    |""")
//}

enum class SampleViewSwitch {
    SOURCE, COMPOSABLE
}