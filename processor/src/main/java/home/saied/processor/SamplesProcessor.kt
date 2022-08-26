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

class SamplesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

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
                    func.annotations.filter { it.shortName.asString() != "Composable" && it.shortName.asString() != "GenSampled" && it.shortName.asString() != "Suppress" }
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
                        func.simpleName.asString() in RuntimeErrorSamples  -> SKIP_BLOCK_GENERATION_REASON.RUNTIME_EXCEPTION
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
                path = file.filePath.substringAfter("GenSampled/"),
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
            annotation.shortName.asString() == "GenSampled"
        }
        return isSampled
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
        codeGenerator.createNewFile(
            Dependencies(false),
            "",
            "samples_report",
            "txt"
        ).use {
            it.bufferedWriter().use { writer ->
                var count = 0
                writer.write("${moduleInfoList.count()} Modules Processed" + "\n")
                moduleInfoList.forEach { sampleModule ->
                    writer.write("  ${sampleModule.moduleName} Module:" + "\n")
                    sampleModule.list.forEach { sampleFileInfo ->
                        writer.write("    ${sampleFileInfo.fileName} File:" + "\n")
                        sampleFileInfo.sampleList.forEach { sample ->
                            writer.write("      ${sample.name} Processed" + "\n")
                            count++
                        }
                    }
                }
                writer.write("\n\n$count Samples Processed")
            }
        }
    }


    /**
     * samples/GenSampled/androidx/compose/runtime/livedata/samples/SamplesGen.kt -> ui
     * support/compose/ui/ui-graphics/samples/src/main/java/androidx/compose/ui/graphics/samples -> ui-graphics
     */
    private fun getModuleName(modulePath: String): String {
        return modulePath.substringAfter("samples/GenSampled/androidx/compose/")
            .substringBefore("/samples").replace('/', '-')
    }

    companion object {
        val RuntimeErrorSamples = setOf("snapshotFlowSample", "DelegatedReadOnlyStateSample")
    }
}

class SamplesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SamplesProcessor(environment.codeGenerator, environment.logger)
}