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

    lateinit var moduleInfoList: List<SampleModuleInfo>
    private val skippedReportItemList: MutableList<String> = mutableListOf()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        moduleInfoList = resolver.getAllFiles().filter { file ->
            logger.warn("sample file:${file.filePath}")
            file.declarations.any(::isSampled)
        }.toList().map { file ->
            val sampleDeclarations =
                file.declarations.filter(::isSampled).map { it as KSFunctionDeclaration }
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
                val funcBody = readDeclarationSourceCode(func)
                val skipBlockGenerationReason: SKIP_BLOCK_GENERATION_REASON? =
                    when {
                        func.parameters.isNotEmpty() -> SKIP_BLOCK_GENERATION_REASON.PARAMETERIZED
                        func.extensionReceiver != null -> SKIP_BLOCK_GENERATION_REASON.EXTENSION_RECEIVER
                        funcBody.contains("R.") -> SKIP_BLOCK_GENERATION_REASON.RESOURCES
                        func.simpleName.asString() == "snapshotFlowSample" -> SKIP_BLOCK_GENERATION_REASON.RUNTIME_EXCEPTION
                        else -> null
                    }.also {
                        if (it != null)
                            skippedReportItemList.add("${func.qualifiedName!!.asString()}:$it")
                    }

                SampleInfo(
                    func.simpleName.asString(),
                    funcBody,
                    func.docString,
                    file.packageName.asString(),
                    annotationSet,
                    skipBlockGeneration = skipBlockGenerationReason
                )
            }.toList()

            SampleFileInfo(
                fileName = file.fileName,
                path = calcRelativePath(file.filePath),
                moduleName = getModuleName(file.filePath),
                packageName = file.packageName.asString(),
                sampleList = sampleInfoList
            )
        }.groupBy {
            it.moduleName
        }.map { (k, list) ->
            SampleModuleInfo(k, list)
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
                    while (i < lineList.size && lineList[i] != "}") {
                        appendLine(lineList[i])
                        i++
                    }
                    appendLine("}")
                }
            }
        }
    }

    private fun isSampled(declaration: KSDeclaration): Boolean {
        val isSampled = declaration.annotations.any { annotation ->
            annotation.shortName.asString() == "Sampled"
        }
        return isSampled
    }

    private fun calcRelativePath(absolutePath: String): String {
        val supportStrIndex = absolutePath.indexOf("support")
        return absolutePath.drop(supportStrIndex)
    }

    @OptIn(KotlinPoetKspPreview::class)
    override fun finish() {
        samplesFileSpec(moduleInfoList).writeTo(
            codeGenerator,
            true
        )
        moduleInfoList.forEach { (k, list) ->
            val moduleName = getModuleName(k)
            logger.warn("writing module file:$k")
            moduleSamplesFileSpec(moduleName, list).writeTo(codeGenerator, true)
        }
        codeGenerator.createNewFile(
            Dependencies(false),
            "",
            "skipped_report",
            "txt"
        ).use {
            it.bufferedWriter().use { writer ->
                skippedReportItemList.forEach { item ->
                    writer.write(item + "\n")
                }
            }
        }
    }


    /**
     * support/compose/ui/ui/samples/src/main/java/androidx/compose/ui/samples -> ui
     * support/compose/ui/ui-graphics/samples/src/main/java/androidx/compose/ui/graphics/samples -> ui-graphics
     */
    private fun getModuleName(modulePath: String): String =
        modulePath.split("/samples")[0].split('/').last()
}

class SamplesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SamplesProcessor(environment.codeGenerator, environment.logger)
}