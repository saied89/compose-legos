package home.saied.composesamples.utils.compose

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, block: Modifier.() -> Modifier) =
    this.then(if (condition) block() else Modifier)