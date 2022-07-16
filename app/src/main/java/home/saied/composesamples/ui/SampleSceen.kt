package home.saied.composesamples.ui

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Dehaze
import androidx.compose.material.icons.outlined.TableRows
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import home.saied.composesamples.ui.codeview.CodeLine
import home.saied.composesamples.utils.compose.thenIf
import home.saied.samples.Sample
import home.saied.samples.SampleWrapper

@OptIn(ExperimentalMaterialApi::class)
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
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    var showSkipBlockgenerationReason by remember { mutableStateOf(true) }
    val observeStateMap = remember { mutableStateMapOf<Any, Any>() }
    var codeScrollable by remember { mutableStateOf(false) }
    BottomSheetScaffold(
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
                    IconButton(
                        onClick = { codeScrollable = !codeScrollable },
                    ) {
                        Icon(
                            imageVector = if (codeScrollable) Icons.Outlined.Dehaze else Icons.Outlined.TableRows,
                            contentDescription = null
                        )
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
                }, shape = androidx.compose.material.MaterialTheme.shapes.small
            ) {
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
        sheetContent = {
            if (sampleViewSwitchState == SampleViewSwitch.COMPOSABLE)
                ObservedStateList(observeStateMap = observeStateMap)
        }
    ) {
        Crossfade(targetState = sampleViewSwitchState, modifier = Modifier.padding(it)) {
            when (it) {
                SampleViewSwitch.SOURCE -> {
                    Code(code = sample.body, scrollable = codeScrollable)
                }
                SampleViewSwitch.COMPOSABLE -> {
                    if (sample.block != null)
                        SampleWrapper(
                            content = {
                                sample.block!!.invoke()
                            }, snapshotStateMap = observeStateMap
                        )
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
private fun Code(code: String, scrollable: Boolean) {
    val lines = remember {
        code.trim().lines()
    }
    val gutterWidth = lines.size.toString().length

    LazyColumn(modifier = Modifier.thenIf(scrollable) { horizontalScroll(rememberScrollState()) }) {
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
private fun ObservedStateList(
    observeStateMap: Map<Any, Any>
) {
    Surface(shape = RectangleShape, modifier = Modifier.heightIn(min = 56.dp)) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state = rememberScrollState()
                    )
                    .padding(start = 16.dp, end = 100.dp, top = 16.dp, bottom = 16.dp)
            ) {
                observeStateMap.forEach { (_, v) ->
                    if (v is State<*>)
                        Text(text = v.value.toString())
                }
            }
        }
    }
}

enum class SampleViewSwitch {
    SOURCE, COMPOSABLE
}