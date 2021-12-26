package home.saied.composesamples.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import home.saied.samples.sampleModules

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun MainScreen() {
    Scaffold() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(moduleList = sampleModules) {
                    navController.navigate("module/$it")
                }
            }
            composable(
                "module/{index}",
                arguments = listOf(navArgument("index") {
                    nullable = false
                    type = NavType.IntType
                })
            ) {
                val index: Int = it.arguments!!.getInt("index")
                ModuleScreen(sampleModule = sampleModules[index]) { fileIndex ->
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
                FileScreen(sampleFile = sampleModules[moduleIndex].sampleFileList[fileIndex]) {

                }
            }
        }
    }
}