package home.saied.composesamples.ideaplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import java.io.BufferedReader
import java.io.InputStreamReader


class LaunchSampleAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val lineText = getSampleName(e)
        executeBashCommand("echo '$lineText'")
        Messages.showInfoMessage(lineText, "Caret Line")
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

private fun executeBashCommand(command: String): Boolean {
    var success = false
    println("Executing BASH command:\n   $command")
    val r = Runtime.getRuntime()
    // Use bash -c so we can handle things like multi commands separated by ; and
    // things like quotes, $, |, and \. My tests show that command comes as
    // one argument to bash, so we do not need to quote it to make it one thing.
    // Also, exec may object if it does not have an executable file as the first thing,
    // so having bash here makes it happy provided bash is installed and in path.
    val commands = arrayOf("bash", "-c", command)
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
        System.err.println("Failed to execute bash with command: $command")
        e.printStackTrace()
    }
    return success
}