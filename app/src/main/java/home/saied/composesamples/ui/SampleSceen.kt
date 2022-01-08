package home.saied.composesamples.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import home.saied.samples.Sample

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
            }, shape = MaterialTheme.shapes.small) {
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
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { /*TODO*/ }, Modifier) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            }
        }
    ) {
        Crossfade(targetState = sampleViewSwitchState, modifier = Modifier.padding(it)) {
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
        code.trim().lines()
    }
    val gutterWidth = lines.size.toString().length
    LazyColumn() {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        itemsIndexed(lines) { index, line ->
            CodeLine(index = index, codeLine = line, gutterWidth = gutterWidth)
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
        }
        item {
            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

@Composable
private fun CodeLine(index: Int, codeLine: String, gutterWidth: Int) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Text(
            text = index.toString().padStart(gutterWidth, '0'),
            style = MaterialTheme.typography.overline,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .align(Top)
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.LightGray)
        )
        Text(
            text = codeLine,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(CenterVertically)
                .padding(horizontal = 4.dp)
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