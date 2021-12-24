package home.saied.composesamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import home.saied.composesamples.ui.SampleItem
import home.saied.samples.*

class MainActivity : ComponentActivity() {

    val sampleList: List<Sample> = sampleListList.flatten()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
//            ComposeSamplesTheme {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    LazyColumn {
                        itemsIndexed(sampleList, itemContent = { index, item ->
                            SampleItem(example = item) {
                                navController.navigate("sample?index=$index")
                            }
                        })
                    }
                }
                composable(
                    "sample?index={index}",
                    arguments = listOf(navArgument("index") {
                        nullable = false
                        type = NavType.IntType
                    })
                ) {
                    val index: Int = it.arguments!!.getInt("index")
                    sampleList[index].block()
                }
            }
        }
//            ComposeSamplesTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color.White
//                ) {
//                    samples[0].block()
//                }
//            }
//        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
val sampleListList = listOf(
    AlignmentLineSampleList,
    AlphaSampleList,
    AndroidViewSampleList,
    BlurSampleList,
    DialogSampleList,
    DrawModifierSampleList,
    FocusSamplesList,
    InspectableModifierSampleList,
    InspectorInfoInComposedModifierSamplesList,
    KeyInputSamplesList,
    LayerModifierSamplesList,
    LayoutSampleList,
    ModifierLocalSamplesList,
    ModifierSamplesList,
    NestedScrollSamplesList,
    OnGloballyPositionedSamplesList,
    OnPlacedSamplesList,
    OnSizeChangedSamplesList,
    PainterSampleList,
    PointerIconSampleList,
    PopupSampleList,
    RotateSampleList,
    ScaleSampleList,
    ShadowSampleList,
    SoftwareKeyboardControllerSampleList,
    SubcomposeLayoutSampleList,
    ZIndexModifierSampleList
)