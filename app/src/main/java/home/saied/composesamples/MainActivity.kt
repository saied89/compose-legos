package home.saied.composesamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.samples.CreateFocusRequesterRefsSample
import home.saied.composesamples.ui.SampleItem
import home.saied.samples.AlignmentLineSampleList

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleItem(example = AlignmentLineSampleList[0], onClick = {})
//            ComposeSamplesTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                ) {
//                    samples[0].block()
//                }
//            }
        }
    }
}
