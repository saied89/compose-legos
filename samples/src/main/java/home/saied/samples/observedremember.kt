package home.saied.samples

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap

@SuppressLint("CompositionLocalNaming")
val rememberRecorderProvider = compositionLocalOf<SnapshotStateMap<Any, Any>> { error("No active user found!") }

@Composable
fun SampleWrapper(content: @Composable () -> Unit, snapshotStateMap: SnapshotStateMap<Any, Any>) {
    CompositionLocalProvider(rememberRecorderProvider provides snapshotStateMap) {
        content()
    }
}

@Composable
inline fun <T> remember(noinline calculation: @DisallowComposableCalls () -> T): T {
    return androidx.compose.runtime.remember(calculation).also {
        currentComposer.recomposeScopeIdentity
        rememberRecorderProvider.current[calculation] = it as Any
    }
}

@Composable
inline fun <T> remember(
    key1: Any?,
    calculation: @DisallowComposableCalls () -> T
): T {
    return androidx.compose.runtime.remember(key1, calculation).also {
        rememberRecorderProvider.current[Unit] = it as Any
    }
}

/**
 * Remember the value returned by [calculation] if [key1] and [key2] are equal to the previous
 * composition, otherwise produce and remember a new value by calling [calculation].
 */
@Composable
inline fun <T> remember(
    key1: Any?,
    key2: Any?,
    calculation: @DisallowComposableCalls () -> T
): T {
    return androidx.compose.runtime.remember(key1, key2, calculation).also {
        rememberRecorderProvider.current[Unit] = it as Any
    }
}

/**
 * Remember the value returned by [calculation] if [key1], [key2] and [key3] are equal to the
 * previous composition, otherwise produce and remember a new value by calling [calculation].
 */
@Composable
inline fun <T> remember(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    calculation: @DisallowComposableCalls () -> T
): T {
    return androidx.compose.runtime.remember(key1, key2, key3, calculation).also {
        rememberRecorderProvider.current[Unit] = it as Any
    }
}

/**
 * Remember the value returned by [calculation] if all values of [keys] are equal to the previous
 * composition, otherwise produce and remember a new value by calling [calculation].
 */
@Composable
inline fun <T> remember(
    vararg keys: Any?,
    calculation: @DisallowComposableCalls () -> T
): T {
    return androidx.compose.runtime.remember(keys = keys, calculation).also {
        rememberRecorderProvider.current[Unit] = it as Any
    }
}