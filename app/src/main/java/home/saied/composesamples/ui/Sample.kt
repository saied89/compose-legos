package home.saied.composesamples.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import home.saied.samples.Sample

@Composable
fun SampleItem(
    example: Sample,
    onClick: (example: Sample) -> Unit
) {
    // TODO: Replace with M3 Card when available
    Surface(
        onClick = { onClick(example) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = ExampleItemBorderWidth,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(modifier = androidx.compose.ui.Modifier.padding(ExampleItemPadding)) {
            Column(modifier = Modifier.weight(1f, fill = true)) {
                Text(
                    text = example.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(ExampleItemTextPadding))
                Text(
                    text = example.name,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(modifier = androidx.compose.ui.Modifier.width(ExampleItemPadding))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

private val ExampleItemPadding = 16.dp
private val ExampleItemTextPadding = 8.dp
private val ExampleItemBorderWidth = 1.dp