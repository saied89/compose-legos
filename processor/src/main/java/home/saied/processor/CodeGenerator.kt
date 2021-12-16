package home.saied.processor

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName
import home.saied.processor_api.Sample
import home.saied.processor_api.SampleFile

val sampleListTypeSpec = List::class.asTypeName().parameterizedBy(Sample::class.asTypeName())

val samplesPropertySpec =
    PropertySpec.builder("samples", sampleListTypeSpec).initializer("listOf()").build()

fun buildSamplesFileSpec(sampleFileList: List<SampleFile>) =
    FileSpec.builder("home.saied.samples", "Samples")
        .addProperty(samplesPropertySpec)
        .build()