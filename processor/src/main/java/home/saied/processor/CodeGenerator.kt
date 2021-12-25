package home.saied.processor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview

const val PACKAGE_NAME = "home.saied.samples"


val composableSlotLambdaName = LambdaTypeName.get(returnType = Unit::class.asTypeName()).copy(
    annotations = listOf(
        AnnotationSpec.builder(ClassName("androidx.compose.runtime", "Composable")).build()
    )
)

val sampleClassSpec = run {

    val flux = FunSpec.constructorBuilder()
        .addParameter("name", String::class)
        .addParameter("body", String::class)
        .addParameter("block", composableSlotLambdaName)
        .build()

    TypeSpec.classBuilder("Sample").primaryConstructor(flux)
        .addProperty(PropertySpec.builder("name", String::class).initializer("name").build())
        .addProperty(PropertySpec.builder("body", String::class).initializer("body").build())
        .addProperty(
            PropertySpec.builder("block", composableSlotLambdaName).initializer("block").build()
        )
        .build()
}

val sampleListTypeSpec =
    List::class.asClassName().parameterizedBy(ClassName(PACKAGE_NAME, "Sample"))

val sampleFileClassSpec = run {
    val flux = FunSpec.constructorBuilder()
        .addParameter("name", String::class)
        .addParameter("sampleList", sampleListTypeSpec)
        .build()
    TypeSpec.classBuilder("SampleFile").primaryConstructor(flux)
        .addProperty(PropertySpec.builder("name", String::class).initializer("name").build())
        .addProperty(
            PropertySpec.builder("sampleList", sampleListTypeSpec).initializer("sampleList").build()
        )
        .build()
}

private fun samplesInitBlock(fileName: String, sampleList: List<SampleInfo>): CodeBlock {
    val builder = CodeBlock.builder().addStatement("%N(%S, buildList {", sampleFileClassSpec, fileName)
    sampleList.forEach { sampleInf ->
        builder.addStatement(
            "    add(%N(%S,%S,{ %M() }))",
            sampleClassSpec,
            sampleInf.name,
            sampleInf.body,
            MemberName(sampleInf.packageName, sampleInf.name)
        )
    }
    return builder.add("})").build()
}

//private fun sampleModulePropertySpec(moduleName: String, sampleFileList: List<SampleFile>): PropertySpec =
//    PropertySpec.builder(moduleName,)
//        .build()

@OptIn(KotlinPoetKspPreview::class)
private fun samplesPropertySpec(sampleFile: SampleFile) =
    PropertySpec.builder(sampleFile.fileName.substringBefore('.'), ClassName(PACKAGE_NAME, "SampleFile"))
        .initializer(samplesInitBlock(sampleFile.fileName, sampleFile.sampleList))
        .apply {
            sampleFile.sampleList.flatMap { it.optInAnnotations }.toSet().forEach(::addAnnotation)
        }
        .build()

fun sampleModelFileSpec(): FileSpec {
    return FileSpec.builder(PACKAGE_NAME, "Samples")
        .addType(sampleClassSpec)
        .addType(sampleFileClassSpec)
        .build()
}

fun moduleSamplesFileSpec(moduleName: String, sampleFileList: List<SampleFile>): FileSpec {
    return FileSpec.builder(PACKAGE_NAME, "${moduleName}Samples")
        .apply {
            sampleFileList.forEach {
                addProperty(samplesPropertySpec(it))
            }
        }
        .build()
}