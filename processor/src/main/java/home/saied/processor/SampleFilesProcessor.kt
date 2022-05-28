package home.saied.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.File

class SampleFilesProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) :
    SymbolProcessor {
    lateinit var sampleFiles: List<KSFile>
    override fun process(resolver: Resolver): List<KSAnnotated> {
        sampleFiles = resolver.getAllFiles().filter { file ->
            file.declarations.any(::isSampled)
        }.toList()

        return emptyList()
    }

    override fun finish() {
        sampleFiles.forEach {
            logger.warn(generatedFilename(it.fileName))
            val res = readDeclarationLines(it) { line ->
                when  {
                    line.startsWith("package") -> "package home.saied.samples.GenSampled.${it.packageName.asString()}"
                    line == "import androidx.compose.runtime.remember" -> "import home.saied.samples.remember"
                    line == "import androidx.annotation.Sampled" -> "import home.saied.samples.gensampled.GenSampled\n" +
                            "import home.saied.samples.R"
                    line.trim() == "@Sampled" -> "@GenSampled"
                    else -> line
                }
            }
            codeGenerator.createNewFile(
                Dependencies(false),
                "home.saied.samples.GenSampled.${it.packageName.asString()}",
                generatedFilename(it.fileName)
            ).bufferedWriter().use { writer ->
                res.forEach {
                    writer.write(it)
                    writer.newLine()
                }
            }
        }
    }

    private fun generatedFilename(filename: String): String {
        val fn = filename.substringBefore('.')
        return "${fn}Gen"
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

    private fun isSampled(declaration: KSDeclaration): Boolean {
        val isSampled = declaration.annotations.any { annotation ->
            annotation.shortName.asString() == "Sampled"
        }
        return isSampled
    }
}

class SampleFilesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SampleFilesProcessor(environment.codeGenerator, environment.logger)
}