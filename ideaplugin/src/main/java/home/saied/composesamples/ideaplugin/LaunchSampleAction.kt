package home.saied.composesamples.ideaplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.util.TextRange
import java.io.BufferedReader
import java.io.InputStreamReader


class LaunchSampleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val lineText = getSampleName(e)
        if (lineText != null)
            launchSample(lineText)
//        Messages.showInfoMessage(lineText, "Caret Line")
    }


    private fun getSampleName(e: AnActionEvent): String? {
        val editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val document = editor.document
        val primaryCaret = editor.caretModel.primaryCaret
        // Get the caret information
        val logicalPos: LogicalPosition = primaryCaret.logicalPosition
        val lineEndOffset = document.getLineEndOffset(logicalPos.line)
        val lineStartOffset = document.getLineStartOffset(logicalPos.line)
        val lineText = document.getText(TextRange(lineStartOffset, lineEndOffset)).trim()
        return if (!lineText.contains("@sample")) null
        else {
            lineText.substringAfter("@sample").trim()
        }
    }

    override fun update(e: AnActionEvent) {
        val lineText = getSampleName(e)
        e.presentation.isVisible = lineText != null
    }
}

private fun launchSample(sampleQualifiedName: String): Boolean {
    val deeplinkCommand =
        "adb shell am start -W -a android.intent.action.VIEW -d sample://composelegos/samples/$sampleQualifiedName"
    var success = false
    println("Executing BASH command:\n   $deeplinkCommand")
    val r = Runtime.getRuntime()
    val commands = arrayOf("bash", "-c", deeplinkCommand)
    try {
        val p = r.exec(commands)
        p.waitFor()
        val b = BufferedReader(InputStreamReader(p.inputStream))
        b.readLines().forEach {
            println(it)
        }
        b.close()
        success = true
    } catch (e: Exception) {
        System.err.println("Failed to execute bash with command: $deeplinkCommand")
        e.printStackTrace()
    }
    return success
}