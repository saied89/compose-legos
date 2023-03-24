package home.saied.composesamples.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R
import home.saied.composesamples.ui.SampleWithPath


@Composable
fun SearchScreen(
    searchRes: List<SampleWithPath>,
    onSearchSampleClick: (SampleWithPath) -> Unit
) {
    LazyColumn {
        items(searchRes) { item ->
            ListItem(
                headlineContent = { Text(text = item.sample.name) },
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
                        onSearchSampleClick(item)
                    }
            )
        }
    }
}