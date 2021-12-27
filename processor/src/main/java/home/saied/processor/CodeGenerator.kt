package home.saied.processor

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview

const val PACKAGE_NAME = "home.saied.samples"


val composableSlotLambdaName = LambdaTypeName.get(returnType = Unit::class.asTypeName()).copy(
    annotations = listOf(
        AnnotationSpec.builder(ClassName("androidx.compose.runtime", "Composable")).build()
    ),
    nullable = true
)

val sampleClassSpec = run {
    val flux = FunSpec.constructorBuilder()
        .addParameter("name", String::class)
        .addParameter("body", String::class)
        .addParameter(ParameterSpec.builder("skipBlockgenerationReason", String::class.asTypeName().copy(nullable = true)).defaultValue("null").build())
        .addParameter(ParameterSpec.builder("block", composableSlotLambdaName).defaultValue("null").build())
        .build()

    TypeSpec.classBuilder("Sample").primaryConstructor(flux)
        .addProperty(PropertySpec.builder("name", String::class).initializer("name").build())
        .addProperty(PropertySpec.builder("body", String::class).initializer("body").build())
        .addProperty(PropertySpec.builder("skipBlockgenerationReason", String::class.asTypeName().copy(nullable = true)).initializer("skipBlockgenerationReason").build())
        .addProperty(
            PropertySpec.builder("block", composableSlotLambdaName).initializer("block").build()
        )
        .addModifiers(KModifier.DATA)
        .build()
}

val sampleFileClassSpec = run {
    val sampleListTypeSpec =
        List::class.asClassName().parameterizedBy(ClassName(PACKAGE_NAME, "Sample"))
    val flux = FunSpec.constructorBuilder()
        .addParameter("name", String::class)
        .addParameter("sampleList", sampleListTypeSpec)
        .build()
    TypeSpec.classBuilder("SampleFile").primaryConstructor(flux)
        .addProperty(PropertySpec.builder("name", String::class).initializer("name").build())
        .addProperty(
            PropertySpec.builder("sampleList", sampleListTypeSpec).initializer("sampleList").build()
        )
        .addModifiers(KModifier.DATA)
        .build()
}

val sampleModuleClassSpec = run {
    val sampleFileListTypeSpec =
        List::class.asClassName().parameterizedBy(ClassName(PACKAGE_NAME, "SampleFile"))
    val flux = FunSpec.constructorBuilder()
        .addParameter("name", String::class)
        .addParameter("sampleFileList", sampleFileListTypeSpec)
        .build()
    TypeSpec.classBuilder("SampleModule").primaryConstructor(flux)
        .addProperty(PropertySpec.builder("name", String::class).initializer("name").build())
        .addProperty(
            PropertySpec.builder("sampleFileList", sampleFileListTypeSpec)
                .initializer("sampleFileList").build()
        )
        .addModifiers(KModifier.DATA)
        .build()
}

private fun moduleInitBlock(moduleName: String, fileList: List<SampleFileInfo>): CodeBlock {
    val builder =
        CodeBlock.builder().addStatement("%N(%S, buildList {", sampleModuleClassSpec, moduleName)
    fileList.forEach { fileInf ->
        builder.addStatement(
            "    add(%M)", MemberName(PACKAGE_NAME, fileInf.fileName.substringBefore('.'))
        )
    }
    return builder.add("})").build()
}

private fun sampleModulePropertySpec(moduleName: String, sampleFileList: List<SampleFileInfo>): PropertySpec =
    PropertySpec.builder(moduleName, ClassName(PACKAGE_NAME, "SampleModule"))
        .initializer(moduleInitBlock(moduleName, sampleFileList))
        .apply {
            sampleFileList.flatMap { it.sampleList }.flatMap { it.optInAnnotations }.toSet().forEach(::addAnnotation)
        }.addModifiers(KModifier.INTERNAL)
        .build()

@OptIn(KotlinPoetKspPreview::class)
private fun samplesPropertySpec(sampleFile: SampleFileInfo): PropertySpec {

    fun samplesInitBlock(fileName: String, sampleList: List<SampleInfo>): CodeBlock {
        val builder =
            CodeBlock.builder().addStatement("%N(%S, buildList {", sampleFileClassSpec, fileName)
        sampleList.forEach { sampleInf ->
            builder.addStatement(
                "    add(%N(%S, %S, block = { %M() }))",
                sampleClassSpec,
                sampleInf.name,
                sampleInf.body,
                MemberName(sampleInf.packageName, sampleInf.name)
            )
        }
        return builder.add("})").build()
    }

    return PropertySpec.builder(
        sampleFile.fileName.substringBefore('.'),
        ClassName(PACKAGE_NAME, "SampleFile")
    )
        .initializer(samplesInitBlock(sampleFile.fileName, sampleFile.sampleList))
        .apply {
            sampleFile.sampleList.flatMap { it.optInAnnotations }.toSet().forEach(::addAnnotation)
        }
        .build()
}

private fun moduleListPropertySpec(moduleList: List<SampleModuleInfo>): PropertySpec {
    val sampleModuleListTypeSpec =
        List::class.asClassName().parameterizedBy(ClassName(PACKAGE_NAME, "SampleModule"))

    fun modulesInitBlock(moduleList: List<SampleModuleInfo>): CodeBlock =
        CodeBlock.builder().addStatement("buildList {")
            .apply {
                moduleList.forEach { module ->
                    val cleanedModulePropertyName = module.moduleName.replace("-", "")
                    addStatement("  add($cleanedModulePropertyName)")
                }
            }
            .addStatement("}")
            .build()
    return PropertySpec.builder("generatedSampleModules", sampleModuleListTypeSpec)
        .initializer(modulesInitBlock(moduleList))
        .apply {
            moduleList.flatMap { it.list }.flatMap { it.sampleList }.flatMap { it.optInAnnotations }.toSet().forEach(::addAnnotation)
        }
        .addModifiers(KModifier.INTERNAL)
        .build()
}


fun samplesFileSpec(moduleList: List<SampleModuleInfo>): FileSpec {
    return FileSpec.builder(PACKAGE_NAME, "Samples")
        .addType(sampleClassSpec)
        .addType(sampleFileClassSpec)
        .addType(sampleModuleClassSpec)
        .addProperty(moduleListPropertySpec(moduleList))
        .build()
}

fun moduleSamplesFileSpec(moduleName: String, sampleFileList: List<SampleFileInfo>): FileSpec {
    val cleanedModuleName = moduleName.replace("-", "")
    return FileSpec.builder(PACKAGE_NAME, "${cleanedModuleName}Samples")
        .apply {
            sampleFileList.forEach {
                addProperty(
                    samplesPropertySpec(it).toBuilder().addModifiers(KModifier.PRIVATE).build()
                )
            }
        }
        .addProperty(sampleModulePropertySpec(cleanedModuleName, sampleFileList))
        .build()
}