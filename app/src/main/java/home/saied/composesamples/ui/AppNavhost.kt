package home.saied.composesamples.ui

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
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import home.saied.composesamples.openUrl
import home.saied.composesamples.sampleSourceUrl
import home.saied.samples.SampleModule
import home.saied.samples.sampleModules

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
                        sampleModules[moduleIndex].sampleFileList[fileIndex].sampleList.first().sourcePath
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
            val sample = sampleModule.sampleFileList[fileIndex].sampleList[sampleIndex]
            SampleScreen(
                sample,
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
            val module = sampleModules.find { it.cleanPackageName == modulePackage }
            val sample = module?.sampleFileList?.flatMap { it.sampleList }
                ?.find { it.name == sampleName }
            val context = LocalContext.current
            if (sample != null)
                SampleScreen(
                    sample = sample,
                    onSourceLaunch = {
                        val sampleSourceUrl = sampleSourceUrl(sample.sourcePath)
                        context.openUrl(sampleSourceUrl)
                    },
                    onBackClick = navController::navigateUp
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