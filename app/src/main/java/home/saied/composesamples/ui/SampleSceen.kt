package home.saied.composesamples.ui

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import home.saied.samples.Sample

@ExperimentalMaterial3Api
@Composable
fun SampleScreen(
    sample: Sample,
    onSourceLaunch: () -> Unit,
    onBackClick: () -> Unit
) {
    var sampleViewSwitchState: SampleViewSwitch by remember {
        mutableStateOf(SampleViewSwitch.SOURCE)
    }
    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }
    var showSkipBlockgenerationReason by remember { mutableStateOf(true) }
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .systemBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    val context = LocalContext.current
                    val localClipboardManager = LocalClipboardManager.current
                    IconButton(
                        onClick = onSourceLaunch
                    ) {
                        Icon(imageVector = Icons.Filled.Launch, contentDescription = null)
                    }
                    IconButton(
                        onClick = {
                            localClipboardManager.setText(AnnotatedString(sample.body))
                            Toast
                                .makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT)
                                .show()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ContentCopy, contentDescription = null)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                sampleViewSwitchState = when (sampleViewSwitchState) {
                    SampleViewSwitch.SOURCE -> {
                        SampleViewSwitch.COMPOSABLE
                    }
                    SampleViewSwitch.COMPOSABLE -> {
                        SampleViewSwitch.SOURCE
                    }
                }
                    showSkipBlockgenerationReason = true
            }, shape = androidx.compose.material.MaterialTheme.shapes.small) {
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
        floatingActionButtonPosition = FabPosition.End
    ) {
        Crossfade(targetState = sampleViewSwitchState, modifier = Modifier.padding(it)) {
            when (it) {
                SampleViewSwitch.SOURCE -> {
                    Code(code = sample.body)
                }
                SampleViewSwitch.COMPOSABLE -> {
                    if (sample.block != null)
                        sample.block!!.invoke()
                    else {
                        if (showSkipBlockgenerationReason)
                            NotGeneratedAlertDialog(reason = sample.skipBlockgenerationReason) {
                                sampleViewSwitchState = SampleViewSwitch.SOURCE
                                showSkipBlockgenerationReason = false
                            }
                    }
                }
            }
        }

    }
}

@Composable
fun NotGeneratedAlertDialog(reason: String?, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Confirm")
            }
        }, text = {
            Text(text = notGeneratedReasonExplanation(reason))
        }
    )
}

private fun notGeneratedReasonExplanation(reason: String?): String =
    when (reason) {
        "PARAMETERIZED" -> "This sample is parameterized. Generating runnable parameterized samples is comming soon"
        "RESOURCES" -> "This sample contains usage of resources. Generating runnable samples that use resources is comming soon"
        "EXTENSION_RECEIVER" -> "This sample is an extension method. Generating runnable extension samples comming soon"
        else -> "This sample is not currently supported"
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
            style = androidx.compose.material.MaterialTheme.typography.overline,
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
            style = androidx.compose.material.MaterialTheme.typography.h5,
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