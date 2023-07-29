package home.saied.composesamples.ui.about

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import home.saied.composesamples.ui.theme.composeBlue
import home.saied.composesamples.ui.theme.composeDarkBlue
import home.saied.composesamples.ui.theme.composeGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.util.UUID

private const val pathData =
    "m1.74,0c-1.14,1.97 -1.74,4.21 -1.74,6.49L0,55.83a9.17,9.17 0,0 0,4.58 7.94l45.08,26.02a9.17," +
            "9.17 0,0 1,4.58 7.94v52.55a9.17,9.17 0,0 0,4.58 7.94l43.17,24.92c1.97,1.14 4.21," +
            "1.74 6.49,1.74L108.48,61.63Z"

@Composable
private fun Logo(
    rotationState: () -> Float,
    modifier: Modifier = Modifier
) {
    val wingPathParser = remember { PathParser().parsePathString(pathData) }
    val wingPath = remember { PathParser().parsePathString(pathData).toPath() }
    val lineToPivot = (wingPathParser.toNodes()[10] as PathNode.LineTo)
    val radius = with(LocalDensity.current) { 60.dp.toPx() }
    val center = Offset(radius, radius)
    val pathPivot = Offset(lineToPivot.x, lineToPivot.y)
    val translation = center - pathPivot
    Canvas(modifier = modifier) {
        val rotation = rotationState()
        val w0Color = if (rotation == 0f) Color.Transparent else composeDarkBlue
        val w1Color = if (rotation > -120) Color.Transparent else composeBlue
        val w2Color: Color =
            if (rotation >= -120)
                lerp(composeDarkBlue, composeBlue, rotation / -120)
            else
                lerp(composeBlue, composeGreen, (rotation + 120) / -120)
        rotate(0f, pivot = center) {
            translate(translation.x, translation.y) {
                drawPath(wingPath, color = w0Color)
            }
        }
        rotate(-120f, pivot = center) {
            translate(translation.x, translation.y) {
                drawPath(wingPath, color = w1Color)
            }
        }
        rotate(rotation, pivot = center) {
            translate(translation.x, translation.y) {
                drawPath(wingPath, color = w2Color)
            }
        }
    }
}

@Preview
@Composable
fun AnimatedLogo(modifier: Modifier = Modifier) {
    var animatedRotation by remember { mutableFloatStateOf(-240f) }
    var restartKey by remember { mutableStateOf("") }
    LaunchedEffect(key1 = restartKey) {
        flow {
            if (restartKey.isEmpty())
                delay(500)
            while (true) {
                emit(Unit)
                delay(4000)
            }
        }.onEach {
            animate(
                0f,
                -240f,
                animationSpec = tween(1000, easing = LinearEasing)
            ) { value, _ ->
                animatedRotation = value
            }
        }.collect()
    }
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Logo(
            rotationState = { animatedRotation },
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null) { restartKey = UUID.randomUUID().toString() }
                .size(120.dp)
                .scale(1.5f)
        )
    }
}