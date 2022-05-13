package home.saied.samples

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.Snapshot

class ScopeRecorder {
    var recomposeScope by mutableStateOf<RecomposeScope?>(null)
}

@Composable
fun StateObserver(
    onUpdate: (Set<Any>) -> Unit,
    content: @Composable ScopeRecorder.() -> Unit
) {
    val scopeRecorder = remember { ScopeRecorder() }
    content(scopeRecorder)
    val contentScope = scopeRecorder.recomposeScope
    DisposableEffect(key1 = content as Any) {
        val subs = Snapshot.registerApplyObserver { set, _ ->
            val reads =
                if (contentScope != null) ReadObserver.observations(contentScope) else emptyArray()
            if (reads.any { it in set })
                onUpdate(reads.toSet())
        }
        onDispose {
            subs.dispose()
        }
    }
}