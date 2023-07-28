package home.saied.composesamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import home.saied.composesamples.ui.graphics.AnimatedLogo
import home.saied.composesamples.ui.graphics.Logo
import home.saied.composesamples.ui.theme.ComposeSamplesTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ComposeSamplesTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    AnimatedLogo(modifier = Modifier.align(Alignment.Center))
                }
//                AppNavHost(sampleModules = sampleModules)
            }
        }
    }
}