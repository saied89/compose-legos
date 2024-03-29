package home.saied.composesamples.ui

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import home.saied.composesamples.openUrl
import home.saied.composesamples.sampleSourceUrl
import home.saied.composesamples.ui.about.About
import home.saied.composesamples.ui.newsamples.NewSamplesScreen
import home.saied.composesamples.ui.newsamples.NewSamplesViewModel
import home.saied.samples.SampleModule

private const val PRIVACY_POLICY_URL = "https://pages.flycricket.io/compose-legos-0/privacy.html"
private const val DEEPLINK_SCHEMA = "sample://composelegos/samples"


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun AppNavHost(sampleModules: List<SampleModule>) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = true)
    }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                moduleList = sampleModules,
                onModuleClick = {
                    navController.navigate("module/$it")
                },
                onSearchSampleClick = {
                    navController.navigate(
                        "module/${it.moduleIndex}",
                        navOptions = navOptions {
                            popUpTo("home")
                        }
                    )
                    navController.navigate(
                        "file/${it.moduleIndex}/${it.fileIndex}"
                    )
                    navController.navigate(
                        "sample/${it.moduleIndex}/${it.fileIndex}/${it.sampleIndex}"
                    )
                },
                onAboutClick = {
                    navController.navigate("about")
                },
                onNewSampleClick = {
                    navController.navigate("new-samples")
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
                onBackClick = navController::navigateUp
            ) { fileIndex ->
                navController.navigate("file/$index/$fileIndex")
            }
        }
        composable(
            "file/{moduleIndex}/{fileIndex}",
            arguments = listOf(
                navArgument("moduleIndex") {
                    nullable = false
                    type = NavType.IntType
                },
                navArgument("fileIndex") {
                    nullable = false
                    type = NavType.IntType
                }
            )
        ) {
            val moduleIndex: Int = it.arguments!!.getInt("moduleIndex")
            val fileIndex: Int = it.arguments!!.getInt("fileIndex")
            val context = LocalContext.current
            FileScreen(
                sampleFile = sampleModules[moduleIndex].sampleFileList[fileIndex],
                moduleName = sampleModules[moduleIndex].name,
                onSampleClicked = { sampleIndex ->
                    navController.navigate("sample/$moduleIndex/$fileIndex/$sampleIndex")
                },
                onSourceLaunch = {
                    val sampleSourceUrl = sampleSourceUrl(
                        sampleModules[moduleIndex].sampleFileList[fileIndex].sampleList.first().sourcePath
                    )
                    context.openUrl(sampleSourceUrl)
                },
                onBackClick = navController::navigateUp
            )
        }
        // TODO add deep link navigation tests
        composable(
            "sample/{moduleIndex}/{fileIndex}/{sampleIndex}",
            arguments = listOf(
                navArgument("moduleIndex") {
                    nullable = false
                    type = NavType.IntType
                },
                navArgument("fileIndex") {
                    nullable = false
                    type = NavType.IntType
                },
                navArgument("sampleIndex") {
                    nullable = false
                    type = NavType.IntType
                }
            )
        ) {
            val moduleIndex: Int = it.arguments!!.getInt("moduleIndex")
            val fileIndex: Int = it.arguments!!.getInt("fileIndex")
            val sampleIndex: Int = it.arguments!!.getInt("sampleIndex")
            val context = LocalContext.current
            val sampleModule = sampleModules[moduleIndex]
            val sampleFile = sampleModule.sampleFileList[fileIndex]
            val sample = sampleFile.sampleList[sampleIndex]
            SampleScreen(
                sample,
                filname = sampleFile.name,
                onFileClick = {
                    navController.popBackStack()
                },
                moduleName = sampleModule.name,
                onModuleClick = {
                    navController.popBackStack()
                    navController.popBackStack()
                },
                onSourceLaunch = {
                    val sampleSourceUrl = sampleSourceUrl(sample.sourcePath)
                    context.openUrl(sampleSourceUrl)
                },
                onBackClick = navController::navigateUp
            )
        }

        composable(
            "sample/{sampleQualifiedName}",
            arguments = listOf(
                navArgument("sampleQualifiedName") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$DEEPLINK_SCHEMA/{sampleQualifiedName}"
                }
            )
        ) { navBackStack ->
            val sampleQualifiedName = navBackStack.arguments?.getString("sampleQualifiedName")
            val sampleName = sampleQualifiedName?.substringAfterLast('.')
            val modulePackage = sampleQualifiedName?.substringBefore(".samples.$sampleName")
            val moduleIndex = sampleModules.indexOfFirst { it.cleanPackageName == modulePackage }
            val fileIndex = sampleModules[moduleIndex].sampleFileList.indexOfFirst { sampleFile ->
                sampleFile.sampleList.any { sample -> sample.name == sampleName }
            }
            val sample = sampleModules[moduleIndex]
                .sampleFileList[fileIndex]
                .sampleList.first { sample ->
                    sample.name == sampleName
                }
            val context = LocalContext.current
            SampleScreen(
                sample = sample,
                filname = sampleModules[moduleIndex].sampleFileList[fileIndex].name,
                onFileClick = {
                    navController.navigate("sample/$moduleIndex/$fileIndex")
                },
                moduleName = sampleModules[moduleIndex].name,
                onModuleClick = {
                    navController.navigate("sample/$moduleIndex")
                },
                onSourceLaunch = {
                    val sampleSourceUrl = sampleSourceUrl(sample.sourcePath)
                    context.openUrl(sampleSourceUrl)
                },
                onBackClick = navController::navigateUp
            )
        }

        composable("new-samples") {
            val application = (LocalContext.current as Activity).application
            val newSamplesViewModel = NewSamplesViewModel(application)
            NewSamplesScreen(
                newSamplesViewModel.newModuleSamples,
                baseLineVersion = newSamplesViewModel.baseLineVersion,
                navigateToSample = { sampleName, moduleName ->
                    val moduleIndex = sampleModules.indexOfFirst { it.name == moduleName }
                    sampleSearchLoop@ for (sampleFileIndex in sampleModules[moduleIndex].sampleFileList.indices)
                        for (sampleIndex in sampleModules[moduleIndex].sampleFileList[sampleFileIndex].sampleList.indices) {
                            val sample =
                                sampleModules[moduleIndex].sampleFileList[sampleFileIndex].sampleList[sampleIndex]
                            if (sample.name == sampleName) {
                                navController.navigate("module/$moduleIndex")
                                navController.navigate("file/$moduleIndex/$sampleFileIndex")
                                navController.navigate("sample/$moduleIndex/$sampleFileIndex/$sampleIndex")
                                break@sampleSearchLoop
                            }
                        }

                },
                onBackPress = navController::navigateUp
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