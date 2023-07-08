package home.saied.composesamples.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
//import androidx.compose.material3.darkColors
//import androidx.compose.material3.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val DarkColorPalette = darkColors(
//    primary = Purple50,
//    primaryVariant = Purple700,
//    secondary = TealLight
//)
//
//private val LightColorPalette = lightColors(
//    primary = TealA100,
//    primaryVariant = TealA100,
//    secondary = Purple50,
//    background = Color.White,
//
//    /* Other default colors to override
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black,
//    */
//

private val colorScheme = lightColorScheme(
    surface = Color.White,
    background = Color.White,
    onSurfaceVariant = Color.Gray
)

@Composable
fun ComposeSamplesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
//        shapes = Shapes,
        content = content
    )
}