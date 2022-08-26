package home.saied.composesamples.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import home.saied.composesamples.openUrl
import home.saied.composesamples.sampleSourceUrl
import home.saied.samples.sampleModules

private val PRIVACY_POLICY_URL = "https://pages.flycricket.io/compose-legos-0/privacy.html"

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun MainScreen() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    }
    Scaffold {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(it)
        ) {
            composable("home") {
                HomeScreen(
                    moduleList = sampleModules,
                    onModuleClick = {
                        navController.navigate("module/$it")
                    },
                    onSearchSampleClick = {
                        navController.navigate(
                            "sample/${it.moduleIndex}/${it.fileIndex}/${it.sampleIndex}"
                        )
                    },
                    onAboutClick = {
                        navController.navigate("about")
                    }
                )
            }
            composable(
                "module/{index}",
                arguments = listOf(navArgument("index") {
                    nullable = false
                    type = NavType.IntType
                })
            ) {
                val index: Int = it.arguments!!.getInt("index")
                ModuleScreen(
                    sampleModule = sampleModules[index],
                    onBackClick = navController::popBackStack
                ) { fileIndex ->
                    navController.navigate("file/$index/$fileIndex")
                }
            }
            composable(
                "file/{moduleIndex}/{fileIndex}",
                arguments = listOf(navArgument("moduleIndex") {
                    nullable = false
                    type = NavType.IntType
                }, navArgument("fileIndex") {
                    nullable = false
                    type = NavType.IntType
                })
            ) {
                val moduleIndex: Int = it.arguments!!.getInt("moduleIndex")
                val fileIndex: Int = it.arguments!!.getInt("fileIndex")
                val context = LocalContext.current
                FileScreen(
                    sampleFile = sampleModules[moduleIndex].sampleFileList[fileIndex],
                    onSampleClicked = { sampleIndex ->
                        navController.navigate("sample/$moduleIndex/$fileIndex/$sampleIndex")
                    },
                    onSourceLaunch = {
                        val sampleSourceUrl = sampleSourceUrl(
                            sampleModules[moduleIndex].sampleFileList[fileIndex].path
                        )
                        context.openUrl(sampleSourceUrl)
                    },
                    onBackClick = navController::popBackStack
                )
            }
            composable(
                "sample/{moduleIndex}/{fileIndex}/{sampleIndex}",
                arguments = listOf(navArgument("moduleIndex") {
                    nullable = false
                    type = NavType.IntType
                }, navArgument("fileIndex") {
                    nullable = false
                    type = NavType.IntType
                }, navArgument("sampleIndex") {
                    nullable = false
                    type = NavType.IntType
                })
            ) {
                val moduleIndex: Int = it.arguments!!.getInt("moduleIndex")
                val fileIndex: Int = it.arguments!!.getInt("fileIndex")
                val sampleIndex: Int = it.arguments!!.getInt("sampleIndex")
                val context = LocalContext.current
                val sampleModule = sampleModules[moduleIndex]
                SampleScreen(
                    sampleModule.sampleFileList[fileIndex].sampleList[sampleIndex],
                    onSourceLaunch = {
                        val sampleSourceUrl = sampleSourceUrl(
                            sampleModule.sampleFileList[fileIndex].path
                        )
                        context.openUrl(sampleSourceUrl)
                    },
                    onBackClick = navController::popBackStack
                )
            }
            dialog("about") {
                val context = LocalContext.current
                About {
                    context.openUrl(PRIVACY_POLICY_URL)
                }
            }
        }
    }
}