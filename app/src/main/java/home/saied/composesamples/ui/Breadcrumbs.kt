package home.saied.composesamples.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

@Composable
fun BreadCrumbsLazyRow(
    overLapFraction: Float,
    currentLocationName: String,
    breadCrumbDetails: List<BreadCrumbDetail>
) {
    val fraction = if (overLapFraction > 0.01f) 1f else 0f
    val appBarContainerColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.containerColor(fraction),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "breadcrumbs color"
    )
    Surface(color = appBarContainerColor) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(bottom = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            itemsIndexed(breadCrumbDetails) { index, item ->
                Text(
                    modifier = Modifier.clickable(onClick = item.onClick),
                    text = item.name,
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null
                )
            }
            item {
                Text(
                    text = currentLocationName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

data class BreadCrumbDetail(val name: String, val onClick: () -> Unit)

@Composable
private fun ColorScheme.containerColor(colorTransitionFraction: Float): Color {
    return lerp(
        surface,
        surfaceColorAtElevation(3.dp),
        FastOutLinearInEasing.transform(colorTransitionFraction)
    )
}
