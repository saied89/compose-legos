package home.saied.composesamples.ui

import android.widget.Toast
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.composesamples.openUrl
import home.saied.samples.SampleFile

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun FileScreen(
    sampleFile: SampleFile,
    onSampleClicked: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = {
                    androidx.compose.material.Text(
                        text = sampleFile.name,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            context.openUrl(sampleFile.path)
                        }
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Launch,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            itemsIndexed(
                sampleFile.sampleList,
                itemContent = { index, item ->
                    ListItem(
                        text = { Text(text = item.name) },
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
                                onSampleClicked(index)
                            }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            )
        }
    }
}