package home.saied.processor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import home.saied.processor_api.SampleFile

const val PACKAGE_NAME = "home.saied.samples"


val composableSlotLambdaName = LambdaTypeName.get(returnType = Unit::class.asTypeName()).copy(
    annotations = listOf(
        AnnotationSpec.builder(ClassName("androidx.compose.runtime", "Composable")).build()
    )
)
val flux = FunSpec.constructorBuilder()
    .addParameter("name", String::class)
    .addParameter("body", String::class)
    .addParameter("block", composableSlotLambdaName)
    .build()

val sampleClassSpec = TypeSpec.classBuilder("Sample").primaryConstructor(flux)
    .addProperty(PropertySpec.builder("name", String::class).initializer("name").build())
    .addProperty(PropertySpec.builder("body", String::class).initializer("body").build())
    .addProperty(PropertySpec.builder("block", composableSlotLambdaName).initializer("block").build())
    .build()

val sampleListTypeSpec =
    List::class.asClassName().parameterizedBy(ClassName(PACKAGE_NAME, "Sample"))

private fun samplesInitBlock(sampleFileList: List<SampleFile>): CodeBlock =
    CodeBlock.builder().add("buildList {\n")
        .addStatement(
            "    add(%N(%S,%S,{ %M() }))",
            sampleClassSpec,
            "greeting",
            "greeting",
            MemberName("androidx.compose.ui.samples", "AndroidViewSample")
        )
        .add("}").build()

val samplesPropertySpec =
    PropertySpec.builder("samples", sampleListTypeSpec).initializer(samplesInitBlock(listOf()))
        .build()

fun buildSamplesFileSpec(sampleFileList: List<SampleFile>): FileSpec {
    return FileSpec.builder(PACKAGE_NAME, "Samples")
        .addType(sampleClassSpec)
        .addProperty(samplesPropertySpec)
        .build()
}