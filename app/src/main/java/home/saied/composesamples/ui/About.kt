package home.saied.composesamples.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import home.saied.composesamples.R

@Composable
fun About(onPrivacyPolicyClick: () -> Unit) {
    Surface(shape = MaterialTheme.shapes.large) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(256.dp)
            )
            Text(
                text = "Generated runnable collection of compose samples",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
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