package home.saied.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.File

class SamplesProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {

    lateinit var sampleFileInfoList: List<SampleFile>

    override fun process(resolver: Resolver): List<KSAnnotated> {
        sampleFileInfoList = resolver.getAllFiles().filter { file ->
            logger.warn("sample file:${file.filePath}")
            file.declarations.any(::isSampledComposable)
        }.toList().map { file ->
            val sampleDeclarations =
                file.declarations.filter(::isSampledComposable).map { it as KSFunctionDeclaration }.filter { it.parameters.isEmpty() }
            val sampleInfoList = sampleDeclarations.map { func ->
                val annotationSet = buildList {
                    func.annotations.filter { it.shortName.asString() != "Composable" && it.shortName.asString() != "Sampled" && it.shortName.asString() != "Suppress" }
                        .forEach {
                            add(
                                ClassName(
                                    it.annotationType.resolve().declaration.packageName.asString(),
                                    it.shortName.asString()
                                )
                            )
                        }
                }
                SampleInfo(
                    func.simpleName.asString(),
                    readDeclarationSourceCode(func),
                    func.docString,
                    file.packageName.asString(),
                    annotationSet
                )
            }.toList()
            SampleFile(file.fileName, sampleInfoList)
        }
        return emptyList()
    }

    private fun readDeclarationSourceCode(declaration: KSFunctionDeclaration): String {
        return declaration.containingFile!!.let {
            val declarationLineNumber = (declaration.location as FileLocation).lineNumber
            File(it.filePath).useLines { lines ->
                val lineList = lines.toList()
                buildString {
                    var i = declarationLineNumber - 1 // Include function signature
                    while (lineList[i] != "}") {
                        appendLine(lineList[i])
                        i++
                    }
                    appendLine("}")
                }
            }
        }
    }

    private fun isSampledComposable(declaration: KSDeclaration): Boolean {
        val isComposable = declaration.annotations.any { annotation ->
            annotation.shortName.asString() == "Composable"
        }
        val isSampled = declaration.annotations.any { annotation ->
            annotation.shortName.asString() == "Composable"
        }
        return isComposable && isSampled
    }

    @OptIn(KotlinPoetKspPreview::class)
    override fun finish() {
        buildSamplesFileSpec(sampleFileInfoList).writeTo(codeGenerator, true)
    }
}

class SamplesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SamplesProcessor(environment.codeGenerator, environment.logger)
}