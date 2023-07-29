package home.saied.composesamples.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun About(onPrivacyPolicyClick: () -> Unit) {
    Surface(shape = MaterialTheme.shapes.large) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedLogo(modifier = Modifier.padding(vertical = 16.dp))
            Text(
                text = "Generated runnable catalogue of compose samples",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            )
            Text(
                text = "Compose Version: 1.5.0-beta03",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
            Text(
                text = "Material3 Version: 1.2.0-alpha02",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            )
            ClickableText(
                text = AnnotatedString("Privacy Policy"),
                style = LocalTextStyle.current.copy(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.padding(vertical = 16.dp),
                onClick = { onPrivacyPolicyClick() }
            )
        }
    }
}