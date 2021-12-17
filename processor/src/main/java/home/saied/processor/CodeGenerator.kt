package home.saied.processor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import home.saied.processor_api.Sample
import home.saied.processor_api.SampleFile

val sampleListTypeSpec = List::class.asTypeName().parameterizedBy(Sample::class.asTypeName())

private fun samplesInitBlock(sampleFileList: List<SampleFile>): CodeBlock =
    CodeBlock.builder().add("buildList {\n")
        .addStatement("    add(%T(%S,%S,%S))", Sample::class, "greeting", "greeting", "greeting")
        .add("}").build()

val samplesPropertySpec =
    PropertySpec.builder("samples", sampleListTypeSpec).initializer(samplesInitBlock(listOf()))
        .build()

fun buildSamplesFileSpec(sampleFileList: List<SampleFile>) =
    FileSpec.builder("home.saied.samples", "Samples")
        .addProperty(samplesPropertySpec)
        .build()