package home.saied.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo

class SamplesProcessor(val codeGenerator: CodeGenerator, val logger: KSPLogger) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAllFiles().filter { file ->
//            logger.warn("sample file:${file.filePath}")
            file.declarations.any { decleration ->
                decleration.annotations.any { annotation ->
                    annotation.shortName.asString() == "Composable"
                }
            }
        }.forEach {
            logger.warn("sample file:", it)
        }
        return emptyList()
    }

    @OptIn(KotlinPoetKspPreview::class)
    override fun finish() {
        buildSamplesFileSpec(listOf()).writeTo(codeGenerator, true)
    }
}

class SamplesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        SamplesProcessor(environment.codeGenerator, environment.logger)
}