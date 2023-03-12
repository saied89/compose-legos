package home.saied.composesamples.ui.codeview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private object Code {
    val simple: SpanStyle = SpanStyle(Color(0xFFA9B7C6))
    val value: SpanStyle = SpanStyle(Color(0xFF6897BB))
    val keyword: SpanStyle = SpanStyle(Color(0xFFCC7832))
    val punctuation: SpanStyle = SpanStyle(Color(0xFFA1C17E))
    val annotation: SpanStyle = SpanStyle(Color(0xFFBBB529))
    val comment: SpanStyle = SpanStyle(Color(0xFF808080))
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: String) {
    addStyle(style, text, Regex.fromLiteral(regexp))
}

private fun AnnotatedString.Builder.addStyle(style: SpanStyle, text: String, regexp: Regex) {
    for (result in regexp.findAll(text)) {
        addStyle(style, result.range.first, result.range.last + 1)
    }
}

private fun codeString(str: String) = buildAnnotatedString {
        val strFormatted = str.replace("\t", "    ")
        append(strFormatted)
        addStyle(Code.punctuation, strFormatted, ":")
        addStyle(Code.punctuation, strFormatted, "=")
        addStyle(Code.punctuation, strFormatted, "\"")
        addStyle(Code.punctuation, strFormatted, "[")
        addStyle(Code.punctuation, strFormatted, "]")
        addStyle(Code.punctuation, strFormatted, "{")
        addStyle(Code.punctuation, strFormatted, "}")
        addStyle(Code.punctuation, strFormatted, "(")
        addStyle(Code.punctuation, strFormatted, ")")
        addStyle(Code.punctuation, strFormatted, ",")
        addStyle(Code.keyword, strFormatted, "fun ")
        addStyle(Code.keyword, strFormatted, "val ")
        addStyle(Code.keyword, strFormatted, "var ")
        addStyle(Code.keyword, strFormatted, "private ")
        addStyle(Code.keyword, strFormatted, "internal ")
        addStyle(Code.keyword, strFormatted, "for ")
        addStyle(Code.keyword, strFormatted, "expect ")
        addStyle(Code.keyword, strFormatted, "actual ")
        addStyle(Code.keyword, strFormatted, "import ")
        addStyle(Code.keyword, strFormatted, "package ")
        addStyle(Code.value, strFormatted, "true")
        addStyle(Code.value, strFormatted, "false")
        addStyle(Code.value, strFormatted, Regex("[0-9]*"))
        addStyle(Code.annotation, strFormatted, Regex("^@[a-zA-Z_]*"))
        addStyle(Code.comment, strFormatted, Regex("^\\s*//.*"))
}

@Composable
fun CodeLine(index: Int, codeLine: String, gutterWidth: Int) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Text(
            text = index.toString().padStart(gutterWidth, '0'),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .align(Alignment.Top)
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.LightGray)
        )
        Text(
            text = codeString(codeLine),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 4.dp)
        )
    }
}

@Preview
@Composable
fun CodeLinePreview() {
    CodeLine(index = 0, codeLine = "test", 1)
}