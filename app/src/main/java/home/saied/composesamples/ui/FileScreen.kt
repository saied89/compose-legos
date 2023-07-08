package home.saied.composesamples.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.samples.SampleFile

@ExperimentalMaterial3Api
@Composable
fun FileScreen(
    sampleFile: SampleFile,
    moduleName: String,
    onSampleClicked: (Int) -> Unit,
    onSourceLaunch: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = sampleFile.name,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onSourceLaunch
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Launch,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                val colorTransitionFraction = scrollBehavior.state.overlappedFraction
                val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
                val appBarContainerColor by animateColorAsState(
                    targetValue = MaterialTheme.colorScheme.containerColor(fraction),
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                )
                Surface(color = appBarContainerColor) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp, start = 16.dp, end = 16.dp)
                    ) {
                        item {
                            Text(
                                modifier = Modifier.clickable(onClick = onBackClick),
                                text = moduleName,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        item {
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null
                            )
                        }
                        item {
                            Text(
                                text = sampleFile.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                sampleFile.sampleList,
                itemContent = { index, item ->
                    ListItem(
                        headlineContent = { Text(text = item.name) },
                        leadingContent = {
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
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColorScheme.containerColor(colorTransitionFraction: Float): Color {
    return lerp(
        surface,
        surfaceColorAtElevation(3.dp),
        FastOutLinearInEasing.transform(colorTransitionFraction)
    )
}