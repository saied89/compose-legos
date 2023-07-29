package home.saied.composesamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import home.saied.composesamples.ui.AppNavHost
import home.saied.composesamples.ui.theme.ComposeSamplesTheme
import home.saied.samples.sampleModules

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ComposeSamplesTheme {
                AppNavHost(sampleModules = sampleModules)
            }
        }
    }
}