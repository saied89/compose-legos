package home.saied.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import java.io.File

class SampleFilesProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) :
    SymbolProcessor {

    private val processedSet = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val allFiles = resolver.getAllFiles()
        logger.warn("process called with ${allFiles.count()} files.")
        allFiles.filter { file ->
            file.declarations.any(::isSampled) && !processedSet.contains(file.uniqueId())
        }.forEach {
            processedSet.add(it.accept(SampleFileVisitor(), Unit))
        }

        return emptyList()
    }

    private fun generatedFilename(filename: String): String {
        return filename.substringBefore('.')
    }

    private fun isSampled(declaration: KSDeclaration): Boolean {
        val isSampled = declaration.annotations.any { annotation ->
            annotation.shortName.asString() == "Sampled"
        }
        return isSampled
    }

    private fun KSFile.uniqueId() = filePath

    inner class SampleFileVisitor : KSDefaultVisitor<Unit, String>() {
        override fun visitFile(file: KSFile, data: Unit): String {
            val res = readDeclarationLines(file) { line ->
                when {
                    line.startsWith("package") -> "package home.saied.samples.GenSampled.${file.packageName.asString()}"
                    line == "import androidx.compose.runtime.remember" -> "import home.saied.samples.remember"
                    line == "import androidx.annotation.Sampled" -> "import home.saied.samples.gensampled.GenSampled\n" +
                            "import home.saied.samples.R"
                    line == "import androidx.paging.compose.itemKey" -> "import androidx.paging.compose.itemKey\n" +
                            "import androidx.paging.compose.samples.TestBackend"
                    line.contains("R.") && !line.contains("android.R.") -> line.replace(
                        "R.",
                        "home.saied.samples.R."
                    )
                    line.trim() == "@Sampled" -> {
                        val pathCleanIndex = file.filePath.indexOf("/support/")
                        val filePath = file.filePath.drop(pathCleanIndex + 9)
                        "@GenSampled(\"$filePath\")"
                    }
                    else -> line
                }
            }
            val message = "home.saied.samples.GenSampled.${file.packageName.asString()}/${
                generatedFilename(file.fileName)
            }"
            logger.warn(message)
            codeGenerator.createNewFile(
                Dependencies(false),
                "home.saied.samples.GenSampled.${file.packageName.asString()}",
                generatedFilename(file.fileName)
            ).bufferedWriter().use { writer ->
                res.forEach {
                    writer.write(it)
                    writer.newLine()
                }
            }
            return file.uniqueId()
        }

        override fun defaultHandler(node: KSNode, data: Unit): String {
            TODO("Not yet implemented")
        }

        private fun readDeclarationLines(
            file: KSFile,
            lineTransform: (String) -> String
        ): List<String> {
            return file.let {
                File(it.filePath).useLines { lines ->
                    lines.toList().map(lineTransform)
                }
            }
        }
    }

    override fun finish() {
        processedSet.forEach {
            File(it).delete()
        }
    }
}

class SampleFilesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SampleFilesProcessor(environment.codeGenerator, environment.logger)
}