package home.saied.composesamples

import android.content.Context
import android.content.Intent
import android.net.Uri

private const val sampleUrlPrefix =
    "https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:"

fun sampleSourceUrl(filePath: String): String {
    return sampleUrlPrefix + filePath
}

fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime/samples/src/main/java/androidx/compose/runtime/samples/EffectSamples.kt?q=EffectSamples
//support/compose/runtime/runtime/samples/src/main/java/androidx/compose/runtime/samples
// androidx/compose/runtime/samples/SideEffectSamples.kt

// https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/AppBarSamples.kt;l=53?q=SimpleSmallTopAppBar
// androidx.compose.material3.samples
// AppBarSamples.kt
// SimpleSmallTopAppBar
// support/compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/AppBarSamples.kt
// compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples/AppBarSamples.kt